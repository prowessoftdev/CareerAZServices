package com.careeraz.services.Service;



import com.careeraz.services.entity.User;
import com.careeraz.services.repo.TokenRespo;
import com.careeraz.services.repo.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    private final String SECRET_KEY = "bc0c23f9a10eb1e89824186287d8d20cfdc33bcd93c3cc4d92c9d404714686da";

    private final TokenRespo tokenRespo;
    private final UserRepository userrespo;

    public JwtService(TokenRespo tokenRespo, UserRepository userrespo) {
        this.tokenRespo = tokenRespo;
        this.userrespo = userrespo;
    }

    // 🔹 Extract email (subject) from token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 🔹 Validate JWT token
    public boolean isValid(String token, UserDetails user) {
        String username = extractUsername(token);

        boolean validToken = tokenRespo
                .findByToken(token)
                .map(t -> !t.isLoggedOut())
                .orElse(false);

        return (username.equals(user.getUsername())) && !isTokenExpired(token) && validToken;
    }

    // 🔹 Check token expiration
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 🔹 Generic claim extractor
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    // 🔹 Parse JWT claims
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigninKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // ✅ Unified userId extractor (reads from claims OR DB)
    public Long extractUserId(String token) {
        try {
            Claims claims = extractAllClaims(token);
            Long userId = claims.get("userId", Long.class);
            if (userId != null) {
                return userId;
            }
        } catch (Exception ignored) {
            // fallback to DB lookup if token doesn't contain userId claim
        }

        // fallback: extract via email
        final String email = extractUsername(token);
        return userrespo.findByEmail(email)
                .map(User::getId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found for token"));
    }

    // 🔹 Generate JWT with email + userId claims
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("userId", user.getId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000)) // 1 day
                .signWith(getSigninKey())
                .compact();
    }

    // 🔹 Decode signing key
    private Key getSigninKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

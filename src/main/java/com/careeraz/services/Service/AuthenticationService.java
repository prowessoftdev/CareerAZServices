package com.careeraz.services.Service;

import com.careeraz.services.entity.AuthenticationResponse;
import com.careeraz.services.entity.Role;
import com.careeraz.services.entity.Token;
import com.careeraz.services.entity.User;
import com.careeraz.services.repo.TokenRespo;
import com.careeraz.services.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final TokenRespo tokenRespo;
    private final AuthenticationManager authenticationManager;
    private final RedisOtpService redisOtpService;
    private final EmailValidationService emailValidationService;

    @Autowired
    public AuthenticationService(UserRepository userRepository,
                                 JwtService jwtService,
                                 TokenRespo tokenRespo,
                                 AuthenticationManager authenticationManager,
                                 EmailService emailService,
                                 RedisOtpService redisOtpService,
                                 EmailValidationService emailValidationService,
                                 PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.tokenRespo = tokenRespo;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
        this.redisOtpService = redisOtpService;
        this.emailValidationService = emailValidationService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Register a new user.
     * Expects a User object; passwords are read from request.getPasswordHash() (raw password)
     * and stored as encoded password into user.passwordHash field.
     *
     * TODO: Replace User input with a RegisterRequest DTO to avoid reusing the entity as input.
     */
    public AuthenticationResponse register(User request) {
        String email = (request.getEmail() == null) ? "" : request.getEmail().toLowerCase().trim();
        if (email.isBlank()) {
            return new AuthenticationResponse(null, "Email must be provided");
        }

        if (userRepository.existsByEmail(email)) {
            return new AuthenticationResponse(null, "User already exists");
        }

        if (!emailValidationService.isEmailValid(email)) {
            return new AuthenticationResponse(null, "Invalid or unreachable email address. Please enter a valid one.");
        }

        // Build user entity from request values we have in your User entity
        User user = User.builder()
                .email(email)
                // Expect request.passwordHash to contain raw password in incoming object (consider renaming later)
                .passwordHash(passwordEncoder.encode(request.getPasswordHash() == null ? "" : request.getPasswordHash()))
                .fullName(request.getFullName())
                .displayName(request.getDisplayName())
                .phone(request.getPhone())
                .locale(request.getLocale())
                .enabled(true)
                .provider(request.getProvider() == null ? com.careeraz.services.entity.AuthProvider.LOCAL : request.getProvider())
                .pictureUrl(request.getPictureUrl())
                .build();

        // Roles assignment: if caller provided Role entities, use them, otherwise keep empty set or fetch default role.
        Set<Role> roles = (request.getRoles() != null && !request.getRoles().isEmpty())
                ? new HashSet<>(request.getRoles())
                : new HashSet<>(); // consider auto-assigning ROLE_USER via RoleRepository
        user.setRoles(roles);

        // Persist user
        User saved = userRepository.save(user);

        // Generate JWT and persist token record
        String jwt = jwtService.generateToken(saved);
        saveUserToken(jwt, saved);

        return new AuthenticationResponse(jwt, "User registration was successful");
    }

    /**
     * Authenticate user with email + password.
     * Raw password expected in request.getPasswordHash().
     */
    public AuthenticationResponse authenticate(User request) {
        String email = (request.getEmail() == null) ? "" : request.getEmail().toLowerCase().trim();
        if (email.isBlank()) {
            throw new UsernameNotFoundException("Email must be provided");
        }

        // Authenticate (will throw BadCredentialsException if invalid)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.getPasswordHash())
        );

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));

        // Optional: verify requested roles exist on the user
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            boolean valid = user.getRoles() != null && user.getRoles().containsAll(request.getRoles());
            if (!valid) {
                throw new UsernameNotFoundException("Invalid role(s) for this user");
            }
        }

        String jwt = jwtService.generateToken(user);

        // Revoke existing tokens and save new one
        revokeAllTokenByUser(user);
        saveUserToken(jwt, user);

        return new AuthenticationResponse(jwt, "User login was successful");
    }

    /**
     * Update basic user profile fields. Uses Long id (User.id type).
     */
    public String updateUser(Long id, String fullName, String displayName, String phone,
                             String pictureUrl, MultipartFile[] images) throws IOException {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (fullName != null) user.setFullName(fullName);
        if (displayName != null) user.setDisplayName(displayName);
        if (phone != null) user.setPhone(phone);
        if (pictureUrl != null) user.setPictureUrl(pictureUrl);

        // NOTE: images handling omitted — implement file storage and save URLs to user if desired.

        userRepository.save(user);
        return "User updated successfully";
    }

    /**
     * Fetch user by id
     */
    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Revoke (mark loggedOut = true) all tokens for the user.
     * TokenRespo expects an Integer userId, so convert Long -> Integer here.
     */
    private void revokeAllTokenByUser(User user) {
        if (user == null || user.getId() == null) return;

        Integer userIdInt;
        try {
            userIdInt = user.getId().intValue();
        } catch (Exception e) {
            // Fallback: if conversion fails, do nothing (but ideally align ID types across entities/repos)
            return;
        }

        List<Token> validTokens = tokenRespo.findAllTokensByUser(userIdInt);
        if (validTokens != null && !validTokens.isEmpty()) {
            validTokens.forEach(t -> t.setLoggedOut(true));
            tokenRespo.saveAll(validTokens);
        }
    }

    private void saveUserToken(String jwt, User user) {
        Token token = new Token();
        token.setToken(jwt);
        token.setLoggedOut(false);
        token.setUser(user);
        tokenRespo.save(token);
    }

    /**
     * Send OTP to existing user email
     */
    public void sendOtp(String email) {
        String normalizedEmail = (email == null) ? "" : email.toLowerCase().trim();
        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String otp = String.valueOf((int) ((Math.random() * 900000) + 100000));
        redisOtpService.saveOtp(normalizedEmail, otp);
        emailService.sendOtpEmail(normalizedEmail, otp);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public String deleteUser(Long id) {
        userRepository.deleteById(id);
        return "deleted";
    }
}

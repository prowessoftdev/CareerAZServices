package com.careeraz.services.config;

import com.careeraz.services.Service.JwtService;
import com.careeraz.services.entity.AuthProvider;
import com.careeraz.services.entity.Role;
import com.careeraz.services.entity.User;
import com.careeraz.services.repo.RoleRepository;
import com.careeraz.services.repo.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    // Role name constants — adjust if your DB uses different names
    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_SERVICE_PROVIDER = "ROLE_SERVICE_PROVIDER";

    public CustomOAuth2SuccessHandler(JwtService jwtService,
                                      UserRepository userRepository,
                                      RoleRepository roleRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        if (email == null || email.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"OAuth provider did not return an email address.\"}");
            return;
        }

        // Decide assigned role based on request URI (your router should call different endpoints for provider vs user)
        String requestURI = request.getRequestURI() == null ? "" : request.getRequestURI();
        String assignedRoleName = requestURI.contains("google-provider") ? ROLE_SERVICE_PROVIDER : ROLE_USER;

        // Fetch or create Role entity
        Role assignedRole = roleRepository.findByName(assignedRoleName)
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setName(assignedRoleName);
                    r.setDescription("Auto-created role: " + assignedRoleName);
                    return roleRepository.save(r);
                });

        Optional<User> existingUserOpt = userRepository.findByEmail(email.toLowerCase().trim());
        User user;
        if (existingUserOpt.isPresent()) {
            user = existingUserOpt.get();
            // Ensure roles set is initialized and contains the assigned role
            Set<Role> roles = user.getRoles();
            if (roles == null) {
                roles = new HashSet<>();
            }
            if (!roles.contains(assignedRole)) {
                roles.add(assignedRole);
                user.setRoles(roles);
            }
        } else {
            user = new User();
            user.setEmail(email.toLowerCase().trim());
            user.setFullName(name != null ? name : "Unknown");
            user.setDisplayName(name != null ? name : "Unknown");
            // For OAuth users, store a placeholder in passwordHash (they won't use it). It's better to keep it random/blank.
            user.setPasswordHash("OAUTH2_USER");
            user.setProvider(AuthProvider.GOOGLE);
            user.setEnabled(true);
            user.setRoles(Collections.singleton(assignedRole));
        }

        // Persist user (insert or update)
        userRepository.save(user);

        // Generate JWT
        String jwtToken = jwtService.generateToken(user);

        // Write response
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        String json = String.format("{\"token\":\"%s\",\"role\":\"%s\"}", jwtToken, assignedRole.getName());
        response.getWriter().write(json);
    }
}

package org.alumni.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.alumni.dto.*;
import org.alumni.model.Role;
import org.alumni.model.User;
import org.alumni.repository.UserRepository;
import org.alumni.security.JwtTokenProvider;
import org.alumni.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Controller for handling all user authentication and registration processes.
 */
@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;

    /**
     * Handles standard user login with email and password.
     * @param loginRequest DTO containing the user's credentials.
     * @return A ResponseEntity with a JWT upon successful authentication.
     */
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthResponse(token));
    }

    /**
     * Handles new user registration.
     * @param signUpRequest DTO containing user registration details.
     * @return A ResponseEntity indicating success or failure.
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(new ApiResponse(false, "Email is already taken!"), HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setName(signUpRequest.getName());
        user.setEmail(signUpRequest.getEmail());
        user.setPhone(signUpRequest.getPhone());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        try {
            Role userRole = Role.valueOf("ROLE_" + signUpRequest.getRole().toUpperCase());
            user.setRole(userRole);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ApiResponse(false, "Error: Invalid role specified."), HttpStatus.BAD_REQUEST);
        }

        switch (user.getRole()) {
            case ROLE_ALUMNI:
                user.setCompanyName(signUpRequest.getCompanyName());
                user.setCompanyRole(signUpRequest.getCompanyRole());
                break;
            case ROLE_STUDENT:
                user.setCollegeName(signUpRequest.getCollegeName());
                user.setBranch(signUpRequest.getBranch());
                user.setCollegeId(signUpRequest.getCollegeId());
                break;
            case ROLE_ADMIN:
                break;
        }

        LOGGER.info("Attempting to save new user with email: {}", user.getEmail());
        User savedUser = userRepository.save(user);
        LOGGER.info("User successfully saved with ID: {}", savedUser.getId());

        return ResponseEntity.ok(new ApiResponse(true, "User registered successfully!"));
    }

    /**
     * Handles login/registration via a social provider like Google.
     * @param socialLoginRequest DTO containing the provider name and the token from the provider.
     * @return A ResponseEntity with this application's own JWT.
     */
    @PostMapping("/social-login")
    public ResponseEntity<JwtAuthResponse> handleSocialLogin(@Valid @RequestBody SocialLoginRequest socialLoginRequest) {
        String provider = socialLoginRequest.getProvider();
        String providerToken = socialLoginRequest.getToken();

        // --- STEP 1: VERIFY TOKEN WITH SOCIAL PROVIDER (e.g., Google) ---
        // In a real application, you would use a Google API client library to verify the 'providerToken'.
        // This call would return the user's details if the token is valid.
        // If invalid, you would throw an exception here.
        // For this example, we'll simulate a successful verification.
        LOGGER.info("Simulating verification for a {} token.", provider);
        // Let's assume verification gives us these details:
        String userEmailFromProvider = "social.user." + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
        String userNameFromProvider = "Social User";
        // --- End of simulation ---

        // --- STEP 2: FIND OR CREATE THE USER IN YOUR DATABASE ---
        User user = userRepository.findByEmail(userEmailFromProvider)
                .orElseGet(() -> {
                    // If the user doesn't exist, create a new account for them.
                    LOGGER.info("User with email {} not found. Creating new account.", userEmailFromProvider);
                    User newUser = new User();
                    newUser.setEmail(userEmailFromProvider);
                    newUser.setName(userNameFromProvider);
                    // Social login users don't have a password in your system. We set a secure, random one.
                    newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));
                    newUser.setRole(Role.ROLE_STUDENT); // Assign a default role
                    return userRepository.save(newUser);
                });

        // --- STEP 3: GENERATE YOUR APPLICATION'S OWN JWT ---
        // We create an Authentication object for the user now trusted by our system.
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String appJwt = jwtTokenProvider.generateToken(authentication);

        LOGGER.info("Generated application JWT for social login user: {}", user.getEmail());
        return ResponseEntity.ok(new JwtAuthResponse(appJwt));
    }

    /**
     * Handles a 'Forgot Password' request.
     * @param forgotPasswordRequest DTO containing the user's email.
     * @return A generic success response to prevent email enumeration attacks.
     */
    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        userRepository.findByEmail(forgotPasswordRequest.getEmail())
                .ifPresent(user -> {
                    // If user is found, generate a link and send the email.
                    String resetLink = "http://your-frontend-app.com/reset-password?token=some-secure-token";
                    try {
                        emailService.sendPasswordResetEmail(user.getEmail(), user.getName(), resetLink);
                        LOGGER.info("Password reset email sent to {}", user.getEmail());
                    } catch (Exception e) {
                        LOGGER.error("Could not send password reset email to {}: {}", user.getEmail(), e.getMessage());
                    }
                });

        // Always return a positive response to prevent attackers from discovering which emails are registered.
        return ResponseEntity.ok(new ApiResponse(true, "If an account with that email exists, a password reset link has been sent."));
    }
}
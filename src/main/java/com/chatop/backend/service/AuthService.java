package com.chatop.backend.service;

import com.chatop.backend.dto.auth.LoginRequestDto;
import com.chatop.backend.dto.auth.RegisterRequestDto;
import com.chatop.backend.entity.User;
import com.chatop.backend.exception.UserAlreadyExistsException;
import com.chatop.backend.exception.UserNotFoundException;
import com.chatop.backend.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for user authentication and registration.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Registers a new user and generates a JWT token.
     *
     * @param registerRequestDto contains the email, name, and password of the user
     * @return a JWT token
     * @throws UserAlreadyExistsException if a user with the given email already exists
     * @throws RuntimeException           if user registration or token generation fails
     */
    @Transactional
    public String registerUser(RegisterRequestDto registerRequestDto) {
        // Check if the user already exists
        if (userRepository.findByEmail(registerRequestDto.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(registerRequestDto.getEmail());
        }

        try {
            // Create the new user
            User user = new User();
            user.setEmail(registerRequestDto.getEmail());
            user.setName(registerRequestDto.getName());
            user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));
            userRepository.save(user);

            // Generate token for the created user
            return generateTokenForUser(
                    registerRequestDto.getEmail(),
                    registerRequestDto.getPassword()
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to register user", e);
        }
    }

    /**
     * Authenticates a user with their email and password and returns a JWT token.
     *
     * @param loginRequestDto contains the email and password for authentication
     * @return a JWT token
     * @throws BadCredentialsException if the email or password is incorrect
     * @throws RuntimeException        if login or token generation fails
     */
    public String loginUser(LoginRequestDto loginRequestDto) {
        try {
            // Check if user exists
            if (userRepository.findByEmail(loginRequestDto.getEmail()).isEmpty()) {
                throw new BadCredentialsException("Invalid credentials");
            }

            // Authenticate and return token
            return generateTokenForUser(
                    loginRequestDto.getEmail(),
                    loginRequestDto.getPassword()
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid credentials");
        } catch (Exception e) {
            throw new RuntimeException("Login failed", e);
        }
    }

    /**
     * Retrieves the currently authenticated user based on the provided JWT token.
     *
     * @param jwtAuthenticationToken the JWT token containing the authenticated user's identity
     * @return the authenticated {@link User}
     * @throws UserNotFoundException if no user is found with the given email
     * @throws RuntimeException      if the token is invalid or missing
     */
    public User getAuthenticatedUser(JwtAuthenticationToken jwtAuthenticationToken) {
        if (jwtAuthenticationToken == null || jwtAuthenticationToken.getName() == null) {
            throw new RuntimeException("Invalid token");
        }

        String authenticatedUserEmail = jwtAuthenticationToken.getName();

        return userRepository.findByEmail(authenticatedUserEmail)
                .orElseThrow(() -> UserNotFoundException.byEmail(authenticatedUserEmail));
    }

    /**
     * Authenticates the user with the provided email and password
     * using the configured {@link AuthenticationManager},
     * then generates and returns a JWT token.
     *
     * @param email    the user's email
     * @param password the user's password
     * @return a JWT token
     * @throws BadCredentialsException if the credentials are invalid
     * @throws RuntimeException        if token generation fails
     */
    private String generateTokenForUser(String email, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            return jwtService.generateToken(authentication);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Invalid credentials");
        } catch (Exception e) {
            throw new RuntimeException("Token generation failed", e);
        }
    }
}

package com.chatop.backend.service;

import com.chatop.backend.dto.Auth.LoginRequestDto;
import com.chatop.backend.dto.Auth.RegisterRequestDto;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public String registerUser(RegisterRequestDto registerRequestDto) {

        // Check if the user exist
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

            return generateTokenForUser(
                    registerRequestDto.getEmail(),
                    registerRequestDto.getPassword()
            );

        } catch (Exception e) {
            throw new RuntimeException("Failed to register user", e);
        }
    }

    public String loginUser(LoginRequestDto loginRequestDto) {
        try {
            // Check if user exists
            if (userRepository.findByEmail(loginRequestDto.getEmail()).isEmpty()) {
                throw new BadCredentialsException("Invalid credentials");
            }

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

    public User getAuthenticatedUser(JwtAuthenticationToken jwtAuthenticationToken) {
        if (jwtAuthenticationToken == null || jwtAuthenticationToken.getName() == null) {
            throw new RuntimeException("Invalid token");
        }

        String authenticatedUserEmail = jwtAuthenticationToken.getName();

        return userRepository.findByEmail(authenticatedUserEmail)
                .orElseThrow(() -> UserNotFoundException.byEmail(authenticatedUserEmail));
    }

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
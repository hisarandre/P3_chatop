package com.chatop.backend.controller;

import com.chatop.backend.dto.auth.LoginRequestDto;
import com.chatop.backend.dto.user.UserResponseDto;
import com.chatop.backend.dto.auth.RegisterRequestDto;
import com.chatop.backend.dto.auth.AuthResponseDto;
import com.chatop.backend.entity.User;
import com.chatop.backend.mapper.UserMapper;
import com.chatop.backend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication and profile retrieval")
public class AuthController {

    private final AuthService authService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    @Operation(
            summary = "Register a new user",
            description = "Create a new user account with email, name, and password.",
            security = {}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "User registered successfully and JWT token returned",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDto.class))
            ),
            @ApiResponse(responseCode = "400",
                    description = "Invalid input data",
                    content = @Content),
            @ApiResponse(responseCode = "409",
                    description = "A user with the provided email already exists",
                    content = @Content)
    })
    public ResponseEntity<AuthResponseDto> registerUser(
            @Parameter(description = "User registration details", required = true)
            @Valid @RequestBody RegisterRequestDto registerRequestDto
    ) {
        String token = authService.registerUser(registerRequestDto);
        return ResponseEntity.ok(new AuthResponseDto(token));
    }

    @PostMapping("/login")
    @Operation(
            summary = "Login user",
            description = "Authenticate a user with email and password to obtain a JWT token.",
            security = {}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "User logged in successfully and JWT token returned",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthResponseDto.class))
            ),
            @ApiResponse(responseCode = "400",
                    description = "Invalid input data",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Invalid credentials",
                    content = @Content)
    })
    public ResponseEntity<AuthResponseDto> loginUser(
            @Parameter(description = "User login credentials", required = true)
            @Valid @RequestBody LoginRequestDto loginRequestDto
    ) {
        String token = authService.loginUser(loginRequestDto);
        return ResponseEntity.ok(new AuthResponseDto(token));
    }

    @GetMapping("/me")
    @Operation(
            summary = "Get current user profile",
            description = "Retrieve the profile information of the currently authenticated user."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "User profile retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class))
            ),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized - Missing or invalid JWT token",
                    content = @Content),
            @ApiResponse(responseCode = "403",
                    description = "Forbidden - Token is expired or lacks required scope",
                    content = @Content)
    })
    @SecurityRequirement(name = "Bearer Authentication")
    public ResponseEntity<UserResponseDto> me(
            @Parameter(hidden = true) JwtAuthenticationToken jwtAuthenticationToken
    ) {
        User user = authService.getAuthenticatedUser(jwtAuthenticationToken);
        return ResponseEntity.ok(userMapper.toUserResponseDto(user));
    }
}

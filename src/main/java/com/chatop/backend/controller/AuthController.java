package com.chatop.backend.controller;

import com.chatop.backend.dto.LoginRequestDto;
import com.chatop.backend.dto.LoginResponseDto;
import com.chatop.backend.dto.RegisterRequestDto;
import com.chatop.backend.dto.AuthResponseDto;
import com.chatop.backend.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> registerUser(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        return ResponseEntity.ok(authService.registerUser(registerRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> loginUser(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authService.loginUser(loginRequestDto));
    }

    @GetMapping("/me")
    public ResponseEntity<LoginResponseDto> me(JwtAuthenticationToken jwtAuthenticationToken) {
        return ResponseEntity.ok(authService.getAuthenticatedUserInfos(jwtAuthenticationToken));
    }

}

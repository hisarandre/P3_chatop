package com.chatop.backend.controller;

import com.chatop.backend.dto.auth.LoginRequestDto;
import com.chatop.backend.dto.user.UserResponseDto;
import com.chatop.backend.dto.auth.RegisterRequestDto;
import com.chatop.backend.dto.auth.AuthResponseDto;
import com.chatop.backend.entity.User;
import com.chatop.backend.mapper.UserMapper;
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
    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> registerUser(@Valid @RequestBody RegisterRequestDto registerRequestDto) {
        String token = authService.registerUser(registerRequestDto);
        AuthResponseDto responseDto = new AuthResponseDto(token);

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> loginUser(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        String token = authService.loginUser(loginRequestDto);
        AuthResponseDto responseDto = new AuthResponseDto(token);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> me(JwtAuthenticationToken jwtAuthenticationToken) {
        User user = authService.getAuthenticatedUser(jwtAuthenticationToken);
        UserResponseDto responseDto = userMapper.toUserResponseDto(user);

        return ResponseEntity.ok(responseDto);
    }

}

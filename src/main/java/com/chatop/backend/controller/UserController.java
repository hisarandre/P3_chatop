package com.chatop.backend.controller;

import com.chatop.backend.dto.user.UserResponseDto;
import com.chatop.backend.entity.User;
import com.chatop.backend.mapper.UserMapper;
import com.chatop.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Integer id) {
        User user = userService.getUserById(id);
        UserResponseDto responseDto = userMapper.toUserResponseDto(user);

        return ResponseEntity.ok(responseDto);
    }

}

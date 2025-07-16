package com.chatop.backend.controller;

import com.chatop.backend.dto.user.UserResponseDto;
import com.chatop.backend.entity.User;
import com.chatop.backend.mapper.UserMapper;
import com.chatop.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Endpoints for retrieving user information")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/{id}")
    @Operation(
            summary = "Get user by ID",
            description = "Retrieve a user's profile information by their ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User found successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content
            )
    })
    public ResponseEntity<UserResponseDto> getUserById(
            @Parameter(description = "The id of the user", required = true)
            @PathVariable Integer id
    ) {
        User user = userService.getUserById(id);
        UserResponseDto responseDto = userMapper.toUserResponseDto(user);

        return ResponseEntity.ok(responseDto);
    }
}

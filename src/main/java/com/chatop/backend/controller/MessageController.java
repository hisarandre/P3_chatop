package com.chatop.backend.controller;

import com.chatop.backend.dto.ApiResponse;
import com.chatop.backend.dto.message.MessageRequestDto;
import com.chatop.backend.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping()
    public ResponseEntity<ApiResponse> addMessage(
            @Valid @RequestBody MessageRequestDto messageRequestDto,
            JwtAuthenticationToken jwtAuthenticationToken
    ) {
        messageService.addMessage(messageRequestDto, jwtAuthenticationToken);
        return ResponseEntity.ok(new ApiResponse("Message send with success"));
    }

}

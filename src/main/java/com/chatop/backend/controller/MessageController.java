package com.chatop.backend.controller;

import com.chatop.backend.dto.MessageResponse;
import com.chatop.backend.dto.message.MessageRequestDto;
import com.chatop.backend.service.MessageService;
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
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@Tag(name = "Messages", description = "Endpoints for managing messages")
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    @Operation(
            summary = "Send a message",
            description = "Send a message associated with a specific rental and user",
            security = {@SecurityRequirement(name = "Bearer Authentication")}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Message sent successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class))
            ),
            @ApiResponse(responseCode = "400",
                    description = "Invalid input data",
                    content = @Content),
            @ApiResponse(responseCode = "401",
                    description = "Unauthorized - Missing or invalid JWT token",
                    content = @Content),
            @ApiResponse(responseCode = "404",
                    description = "Rental not found",
                    content = @Content)
    })
    public ResponseEntity<MessageResponse> addMessage(
            @Parameter(description = "Message details including rental ID", required = true)
            @Valid @RequestBody MessageRequestDto messageRequestDto,
            @Parameter(hidden = true) JwtAuthenticationToken jwtAuthenticationToken
    ) {
        messageService.addMessage(messageRequestDto, jwtAuthenticationToken);
        return ResponseEntity.ok(new MessageResponse("Message sent with success"));
    }
}

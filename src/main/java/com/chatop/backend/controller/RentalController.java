package com.chatop.backend.controller;

import com.chatop.backend.dto.MessageResponse;
import com.chatop.backend.dto.rental.RentalCreateRequestDto;
import com.chatop.backend.dto.rental.RentalResponseDto;
import com.chatop.backend.dto.rental.RentalUpdateRequestDto;
import com.chatop.backend.dto.rental.RentalsResponseDto;
import com.chatop.backend.entity.Rental;
import com.chatop.backend.mapper.RentalMapper;
import com.chatop.backend.service.RentalService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
@Tag(name = "Rentals", description = "Endpoints for managing rentals")
public class RentalController {

    private final RentalService rentalService;
    private final RentalMapper rentalMapper;

    @GetMapping()
    @Operation(summary = "Get all rentals", description = "Retrieve a list of all available rentals.")
    @ApiResponse(
            responseCode = "200",
            description = "List of rentals retrieved successfully",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = RentalsResponseDto.class))
    )
    public ResponseEntity<RentalsResponseDto> getAllRentals() {
        List<Rental> rentals = rentalService.getAllRentals();
        RentalsResponseDto responseDto = rentalMapper.toRentalsResponseDto(rentals);
        return ResponseEntity.ok(responseDto);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Create rental",
            description = "Create a new rental. Requires an authenticated user.",
            security = {@SecurityRequirement(name = "Bearer Authentication")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Rental created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class))
            ),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    public ResponseEntity<MessageResponse> createRental(
            @Parameter(description = "Rental creation details", required = true)
            @Valid @ModelAttribute RentalCreateRequestDto rentalDto,
            @Parameter(hidden = true) JwtAuthenticationToken jwtAuthenticationToken
    ) throws IOException {
        rentalService.createRental(rentalDto, jwtAuthenticationToken);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Rental created !"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get rental by ID", description = "Retrieve rental details by rental ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Rental found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = RentalResponseDto.class))
            ),
            @ApiResponse(responseCode = "404", description = "Rental not found", content = @Content)
    })
    public ResponseEntity<RentalResponseDto> getRentalById(
            @Parameter(description = "The ID of the rental", required = true) @PathVariable Integer id
    ) {
        Rental rental = rentalService.getRentalById(id);
        RentalResponseDto responseDto = rentalMapper.toRentalResponseDto(rental);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update rental",
            description = "Update an existing rental. Only the owner of the rental can update it.",
            security = {@SecurityRequirement(name = "Bearer Authentication")}
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Rental updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageResponse.class))
            ),
            @ApiResponse(responseCode = "403", description = "Forbidden: not your rental", content = @Content),
            @ApiResponse(responseCode = "404", description = "Rental not found", content = @Content)
    })
    public ResponseEntity<MessageResponse> updateRental(
            @Parameter(description = "The ID of the rental to update", required = true) @PathVariable Integer id,
            @Parameter(description = "Rental update details", required = true)
            @Valid @ModelAttribute RentalUpdateRequestDto rentalDto,
            @Parameter(hidden = true) JwtAuthenticationToken jwtAuthenticationToken
    ) throws IOException {
        rentalService.updateRental(id, rentalDto, jwtAuthenticationToken);
        return ResponseEntity.ok(new MessageResponse("Rental updated !"));
    }
}

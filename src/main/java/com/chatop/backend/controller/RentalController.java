package com.chatop.backend.controller;

import com.chatop.backend.dto.ApiResponse;
import com.chatop.backend.dto.rental.RentalCreateRequestDto;
import com.chatop.backend.dto.rental.RentalResponseDto;
import com.chatop.backend.dto.rental.RentalUpdateRequestDto;
import com.chatop.backend.dto.rental.RentalsResponseDto;
import com.chatop.backend.entity.Rental;
import com.chatop.backend.mapper.RentalMapper;
import com.chatop.backend.service.RentalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@RestController
@RequestMapping("/api/rentals")
@RequiredArgsConstructor
public class RentalController {

    private final RentalService rentalService;
    private final RentalMapper rentalMapper;

    @GetMapping()
    public ResponseEntity<RentalsResponseDto> getAllRentals() {
        List<Rental> rentals = rentalService.getAllRentals();
        RentalsResponseDto responseDto = rentalMapper.toRentalsResponseDto(rentals);

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createRental(
            @Valid @ModelAttribute RentalCreateRequestDto rentalDto,
            JwtAuthenticationToken jwtAuthenticationToken
    ) throws IOException {
        rentalService.createRental(rentalDto, jwtAuthenticationToken);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("Rental created !"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalResponseDto> getRentalById(@PathVariable Integer id) {
        Rental rental = rentalService.getRentalById(id);
        RentalResponseDto responseDto = rentalMapper.toRentalResponseDto(rental);

        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateRental(
            @PathVariable Integer id,
            @Valid @ModelAttribute RentalUpdateRequestDto rentalDto,
            JwtAuthenticationToken jwtAuthenticationToken
            ) throws IOException {
        rentalService.updateRental(id, rentalDto, jwtAuthenticationToken);
        return ResponseEntity.ok(new ApiResponse("Rental updated !"));
    }

}

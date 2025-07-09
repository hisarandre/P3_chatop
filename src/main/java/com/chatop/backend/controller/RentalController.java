package com.chatop.backend.controller;

import com.chatop.backend.dto.Rental.RentalCreateRequestDto;
import com.chatop.backend.dto.Rental.RentalResponseDto;
import com.chatop.backend.dto.Rental.RentalsResponseDto;
import com.chatop.backend.dto.UserResponseDto;
import com.chatop.backend.entity.Rental;
import com.chatop.backend.mapper.RentalMapper;
import com.chatop.backend.service.RentalService;
import com.chatop.backend.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;
import java.util.UUID;


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
    public ResponseEntity<Map<String, String>> createRental(
            @Valid @ModelAttribute RentalCreateRequestDto rentalDto,
            JwtAuthenticationToken jwtAuthenticationToken
    ) throws IOException {
        rentalService.createRental(rentalDto, jwtAuthenticationToken);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Rental created !"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalResponseDto> getRentalById(@PathVariable Integer id) {
        Rental rental = rentalService.getRentalById(id);
        RentalResponseDto responseDto = rentalMapper.toRentalResponseDto(rental);

        return ResponseEntity.ok(responseDto);
    }

    //@PutMapping("/{id}")
    //public ResponseEntity<UserResponseDto> updateRental(@PathVariable Integer id) {
    //    return ResponseEntity.ok(rentalService.updateRental(id));
    //}


}

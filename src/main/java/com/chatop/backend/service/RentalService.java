package com.chatop.backend.service;
import com.chatop.backend.dto.Rental.RentalCreateRequestDto;
import com.chatop.backend.entity.Rental;
import com.chatop.backend.entity.User;
import com.chatop.backend.exception.InvalidFileTypeException;
import com.chatop.backend.exception.UserNotFoundException;
import com.chatop.backend.mapper.RentalMapper;
import com.chatop.backend.repository.RentalRepository;
import com.chatop.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;
    private final UserRepository userRepository;

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    @Transactional
    public void createRental(RentalCreateRequestDto rentalDto, JwtAuthenticationToken jwtAuthenticationToken) throws IOException {
        MultipartFile pictureFile = rentalDto.getPicture();

        // Validate file type
        Set<String> allowedTypes = Set.of("image/jpeg", "image/png", "image/webp");
        if (!allowedTypes.contains(pictureFile.getContentType())) {
            throw InvalidFileTypeException.forType(pictureFile.getContentType());
        }

        String fileName = UUID.randomUUID() + "_" + pictureFile.getOriginalFilename();
        Path uploadPath = Paths.get("upload");

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(pictureFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        User user = userRepository.findByEmail(jwtAuthenticationToken.getName())
                .orElseThrow(() -> UserNotFoundException.byEmail(jwtAuthenticationToken.getName()));

        // Map Dto to Entity
        Rental rental = rentalMapper.createRentalDtoToEntity(rentalDto);
        rental.setPicture("/upload/" + fileName);
        rental.setOwner(user);

        rentalRepository.save(rental);
    }

    public Rental getRentalById(Integer id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rental not found with id: " + id));
    }

    //public RentalResponseDto updateRental(@PathVariable Integer id) {
    //    return ResponseEntity.ok(rentalService.getRentalById(id));
    //}




}
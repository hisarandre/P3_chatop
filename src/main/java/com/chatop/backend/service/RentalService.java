package com.chatop.backend.service;

import com.chatop.backend.dto.rental.RentalCreateRequestDto;
import com.chatop.backend.dto.rental.RentalUpdateRequestDto;
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
import java.nio.file.*;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * Service layer for managing rentals operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;
    private final UserRepository userRepository;

    private static final Set<String> ALLOWED_TYPES = Set.of("image/jpeg", "image/png", "image/webp");
    private static final Path UPLOAD_PATH = Paths.get("upload");

    /**
     * Retrieve all rentals from the database.
     *
     * @return list of {@link Rental} entities
     */
    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    /**
     * Create a new rental.
     *
     * @param rentalDto data for creating the rental
     * @param token authenticated user's JWT token
     * @throws IOException if there is an error while saving the uploaded picture
     * @throws UserNotFoundException if the user cannot be found
     */
    @Transactional
    public void createRental(RentalCreateRequestDto rentalDto, JwtAuthenticationToken token) throws IOException {
        User user = userRepository.findByEmail(token.getName())
                .orElseThrow(() -> UserNotFoundException.byEmail(token.getName()));

        String picturePath = handlePictureUpload(rentalDto.getPicture());

        Rental rental = rentalMapper.createRentalDtoToEntity(rentalDto);
        rental.setPicture(picturePath);
        rental.setOwner(user);

        rentalRepository.save(rental);
    }

    /**
     * Retrieve a rental by its ID.
     *
     * @param id the rental ID
     * @return the {@link Rental} entity
     * @throws EntityNotFoundException if the rental does not exist
     */
    public Rental getRentalById(Integer id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rental not found with id: " + id));
    }

    /**
     * Update an existing rental.
     *
     * @param id the rental ID
     * @param rentalDto the update data
     * @param token the authenticated user's token
     * @throws IOException if there is an error handling file uploads
     * @throws EntityNotFoundException if the rental is not found
     * @throws AccessDeniedException if the user is not the owner
     */
    @Transactional
    public void updateRental(Integer id, RentalUpdateRequestDto rentalDto, JwtAuthenticationToken token) throws IOException {
        Rental existingRental = rentalRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Rental not found with id: " + id));

        User user = userRepository.findByEmail(token.getName())
                .orElseThrow(() -> UserNotFoundException.byEmail(token.getName()));

        // Verify if the user created the rental
        if (!existingRental.getOwner().getId().equals(user.getId())) {
            throw new AccessDeniedException("You can only update your own rental");
        }

        // Update the existing entity
        rentalMapper.updateRentalFromDto(rentalDto, existingRental);
        rentalRepository.save(existingRental);
    }

    /**
     * Validates and saves an uploaded picture to the filesystem.
     *
     * @param newPicture the uploaded file
     * @return the relative path to the saved picture
     * @throws IOException if saving fails
     * @throws InvalidFileTypeException if the file type is not supported
     */
    private String handlePictureUpload(MultipartFile newPicture) throws IOException {
        if (!ALLOWED_TYPES.contains(newPicture.getContentType())) {
            throw InvalidFileTypeException.forType(newPicture.getContentType());
        }

        // Create upload folder if not exist
        if (!Files.exists(UPLOAD_PATH)) {
            Files.createDirectories(UPLOAD_PATH);
        }

        String newFileName = UUID.randomUUID() + "_" + newPicture.getOriginalFilename();
        Path targetPath = UPLOAD_PATH.resolve(newFileName);

        Files.copy(newPicture.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return "/upload/" + newFileName;
    }
}
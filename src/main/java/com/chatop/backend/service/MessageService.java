package com.chatop.backend.service;

import com.chatop.backend.dto.message.MessageRequestDto;
import com.chatop.backend.entity.Message;
import com.chatop.backend.entity.Rental;
import com.chatop.backend.entity.User;
import com.chatop.backend.exception.UserNotFoundException;
import com.chatop.backend.mapper.MessageMapper;
import com.chatop.backend.repository.MessageRepository;
import com.chatop.backend.repository.RentalRepository;
import com.chatop.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * Service responsible for handling message for a specific rental and authenticated user.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final MessageMapper messageMapper;

    /**
     * Creates and saves a new message.
     *
     * @param messageRequestDto       the DTO containing the message content, rental ID and user ID
     * @param jwtAuthenticationToken  the authenticated user's token
     * @throws EntityNotFoundException if the rental specified does not exist
     * @throws UserNotFoundException   if the authenticated user cannot be found
     */
    public void addMessage(MessageRequestDto messageRequestDto, JwtAuthenticationToken jwtAuthenticationToken) {
        Message message = messageMapper.messageRequestDtoToEntity(messageRequestDto);

        Rental rental = rentalRepository.findById(messageRequestDto.getRental_id())
                .orElseThrow(() -> new EntityNotFoundException("Rental not found"));
        message.setRental(rental);

        User user = userRepository.findByEmail(jwtAuthenticationToken.getName())
                .orElseThrow(() -> UserNotFoundException.byEmail(jwtAuthenticationToken.getName()));
        message.setUser(user);

        messageRepository.save(message);
    }
}
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


@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final MessageMapper messageMapper;

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
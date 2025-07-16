package com.chatop.backend.service;

import com.chatop.backend.entity.User;
import com.chatop.backend.exception.UserNotFoundException;
import com.chatop.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Service responsible for user-related operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * Retrieves a user by their ID.
     *
     * @param id the id of the user
     * @return the {@link User} entity
     * @throws UserNotFoundException if no user exists with the provided ID
     */
    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> UserNotFoundException.byId(id));
    }
}

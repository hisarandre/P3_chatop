package com.chatop.backend.service;
import com.chatop.backend.entity.User;
import com.chatop.backend.exception.UserNotFoundException;
import com.chatop.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> UserNotFoundException.byId(id));
    }
}
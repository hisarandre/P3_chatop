package com.chatop.backend.repository;

import com.chatop.backend.entity.Message;
import com.chatop.backend.entity.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
}
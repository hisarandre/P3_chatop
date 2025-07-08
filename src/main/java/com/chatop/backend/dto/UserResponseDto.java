package com.chatop.backend.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    Integer id;
    String name;
    String email;

    @JsonFormat(pattern = "yyyy/MM/dd")
    LocalDateTime created_at;
    @JsonFormat(pattern = "yyyy/MM/dd")
    LocalDateTime updated_at;
}
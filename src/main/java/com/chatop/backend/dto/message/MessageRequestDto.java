package com.chatop.backend.dto.message;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequestDto {
    @NotBlank
    @Size(max = 2000)
    String message;

    @NotNull
    Integer rental_id;

    @NotNull
    Integer user_id;
}

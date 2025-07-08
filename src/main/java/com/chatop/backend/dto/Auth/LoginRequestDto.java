package com.chatop.backend.dto.Auth;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

    @Email
    @NotBlank
    @Size(min = 3, max = 255)
    String email;

    @NotBlank
    @Size(min = 6, max = 255)
    String password;
}
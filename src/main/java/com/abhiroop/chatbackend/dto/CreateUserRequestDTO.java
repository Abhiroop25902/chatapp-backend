package com.abhiroop.chatbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserRequestDTO(
        @NotBlank(message = "Username is required")
        @Email(message = "Invalid email format")
        String email,
        @NotBlank(message = "Password is required")
        String password) {
}

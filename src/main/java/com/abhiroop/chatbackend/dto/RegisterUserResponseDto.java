package com.abhiroop.chatbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record RegisterUserResponseDto(
        @NotBlank(message = "id must be present while returning User Data")
        UUID id,
        String username,
        @NotBlank(message = "email must be present while returning User Data")
        @Email(message = "wrong email format")
        String email) {

}

package com.abhiroop.chatbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record LoginUserResponseDto(
        @NotBlank
        UUID uuid,
        @NotBlank
        @Email
        String email,
        @NotBlank
        String token,
        @NotBlank
        String refreshToken

) {
}

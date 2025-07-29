package com.abhiroop.chatbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginUserRequestDto(
        @NotBlank(message = "Email must be present")
        @Email(message = "Email format is not correct")
        String email,
        
        @NotBlank(message = "Password must be present")
        String password
) {
}

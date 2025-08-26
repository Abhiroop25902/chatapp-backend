package com.abhiroop.chatbackend.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record MessagePatchRequestDto(
        @NotBlank(message = "messageId is required")
        UUID messageId,
        @NotBlank(message = "content is required")
        String content
) {
}

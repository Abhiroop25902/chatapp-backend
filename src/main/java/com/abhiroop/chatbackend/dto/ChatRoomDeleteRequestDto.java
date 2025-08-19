package com.abhiroop.chatbackend.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record ChatRoomDeleteRequestDto(
        @NotBlank
        UUID chatRoomId
) {
}

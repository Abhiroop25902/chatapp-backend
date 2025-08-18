package com.abhiroop.chatbackend.dto;

import jakarta.validation.constraints.Size;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record ChatRoomPatchRequestDto(
        @NotNull("Chat Room Id is Required")
        UUID chatRoomId,
        @Size(max = 255, message = "Room name cannot exceed 255 characters")
        String newRoomName,
        @Size(max = 255, message = "Room Description cannot exceed 255 characters")
        String newRoomDescription
) {
}

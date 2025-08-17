package com.abhiroop.chatbackend.dto;

import com.abhiroop.chatbackend.lib.enums.RoomType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChatRoomCreateRequestDto(

        @NotBlank(message = "Room name is required")
        String roomName,
        @NotNull(message = "Room Type is required")
        RoomType roomType,
        String roomDescription
) {
}

package com.abhiroop.chatbackend.dto;

import com.abhiroop.chatbackend.lib.enums.RoomType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record ChatRoomCreateRequestDto(

        @NotBlank(message = "Room name is required")
        @Size(max = 255, message = "Room Name cannot exceed 255 charactes")
        String roomName,
        @NotNull(message = "Room Type is required")
        RoomType roomType,
        @Size(max = 255, message = "Room Description cannot exceed 255 characters")
        String roomDescription,
        UUID otherUserId
) {

    @AssertTrue(message = "DIRECT_MESSAGE room must have otherUserId; other rooms cannot have it")
    private boolean isOtherUserIdValid() {
        if (roomType == RoomType.DIRECT_MESSAGE) {
            return otherUserId != null; // must be present
        } else {
            return otherUserId == null; // must NOT be present
        }
    }
}

package com.abhiroop.chatbackend.dto;

import com.abhiroop.chatbackend.lib.enums.RoomType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ChatRoomCreateOrUpdateResponseDto(
        UUID chatRoomId,
        String chatRoomName,
        String chatRoomDescription,
        RoomType roomType,
        UUID createdBy,
        boolean isActive,
        int maxParticipant,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

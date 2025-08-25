package com.abhiroop.chatbackend.dto;

import com.abhiroop.chatbackend.lib.enums.MessageType;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;


@Builder
public record MessageCreateOrUpdateResponseDto(
        UUID messageId,
        UUID chatRoomId,
        UUID senderId,
        MessageType messageType,
        String content,
        UUID replyToMessageId,
        LocalDateTime sentAt,
        LocalDateTime editedAt
) {
}

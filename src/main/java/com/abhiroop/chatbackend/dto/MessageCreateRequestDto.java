package com.abhiroop.chatbackend.dto;

import com.abhiroop.chatbackend.lib.enums.MessageType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MessageCreateRequestDto(
        @NotBlank(message = "message must have some content")
        String content,
        @NotNull(message = "messageType must be present")
        MessageType messageType,
        @NotNull(message = "message needs a chatRoom to go to")
        UUID chatRoomId,
        UUID replyMessageId

) {
}

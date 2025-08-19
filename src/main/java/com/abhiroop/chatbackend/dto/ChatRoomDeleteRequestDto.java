package com.abhiroop.chatbackend.dto;

import java.util.UUID;

public record ChatRoomDeleteRequestDto(
        UUID chatRoomId
) {
}

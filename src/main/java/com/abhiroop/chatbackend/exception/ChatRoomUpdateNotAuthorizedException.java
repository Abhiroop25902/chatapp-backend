package com.abhiroop.chatbackend.exception;

import java.util.UUID;

public class ChatRoomUpdateNotAuthorizedException extends RuntimeException {
    final UUID userId;

    public ChatRoomUpdateNotAuthorizedException(String message, UUID userId) {
        super(message);
        this.userId = userId;
    }
}

package com.abhiroop.chatbackend.exception;

import java.util.UUID;

public class ChatRoomEditNotAuthorizedException extends RuntimeException {
    final UUID userId;

    public ChatRoomEditNotAuthorizedException(String message, UUID userId) {
        super(message);
        this.userId = userId;
    }
}

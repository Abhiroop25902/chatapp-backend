package com.abhiroop.chatbackend.exception;

import java.util.UUID;

public class ChatRoomNotFoundException extends RuntimeException {
    final UUID chatRoomId;

    public ChatRoomNotFoundException(String message, UUID chatRoomId) {
        super(message);
        this.chatRoomId = chatRoomId;
    }
}

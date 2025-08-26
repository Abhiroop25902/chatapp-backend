package com.abhiroop.chatbackend.exception;

import java.util.UUID;

public class MessageNotFoundException extends RuntimeException {
    final UUID messageId;

    public MessageNotFoundException(String message, UUID messageId) {
        super(message);
        this.messageId = messageId;
    }
}

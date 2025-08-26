package com.abhiroop.chatbackend.exception;

import java.util.UUID;

public class MessageEditNotAuthorizedException extends RuntimeException {
    final UUID editorUserId;
    final UUID messageId;

    public MessageEditNotAuthorizedException(String message, UUID editorUserId, UUID messageId) {
        super(message);
        this.editorUserId = editorUserId;
        this.messageId = messageId;
    }
}

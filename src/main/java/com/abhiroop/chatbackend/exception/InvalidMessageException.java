package com.abhiroop.chatbackend.exception;

import lombok.Getter;

import java.util.UUID;

@Getter
public class InvalidMessageException extends RuntimeException {
    private final UUID userId;
    private final UUID chatRoomId;

    public InvalidMessageException(String message, UUID userId, UUID chatRoomId) {
        super(message);
        this.userId = userId;
        this.chatRoomId = chatRoomId;
    }
}

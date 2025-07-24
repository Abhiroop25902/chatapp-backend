package com.abhiroop.chatbackend.exception;

public class UserNameGenerationFailureException extends RuntimeException {
    public UserNameGenerationFailureException(String message) {
        super(message);
    }
}

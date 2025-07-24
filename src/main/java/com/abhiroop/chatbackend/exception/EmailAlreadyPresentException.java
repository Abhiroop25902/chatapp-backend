package com.abhiroop.chatbackend.exception;

public class EmailAlreadyPresentException extends RuntimeException {
    public EmailAlreadyPresentException(String message) {
        super(message);
    }
}

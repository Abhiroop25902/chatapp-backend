package com.abhiroop.chatbackend.exception;

public class PasswordValidationFailException extends RuntimeException {
    public PasswordValidationFailException(String message) {
        super(message);
    }
}

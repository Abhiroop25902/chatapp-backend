package com.abhiroop.chatbackend.exception;

import lombok.Getter;

@Getter
public class UnauthorizedUserException extends RuntimeException {
    private final String email;

    public UnauthorizedUserException(String email, String message) {
        super(message);
        this.email = email;
    }
}

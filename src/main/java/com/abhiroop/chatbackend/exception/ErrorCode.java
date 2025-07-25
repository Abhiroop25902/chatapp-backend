package com.abhiroop.chatbackend.exception;

public enum ErrorCode {
    VALIDATION_FAILED("VALIDATION_FAILED"),
    CONSTRAINT_VALIDATION_FAILED("CONSTRAINT_VALIDATION_FAILED"),
    DATA_INTEGRITY_VALIDATION_FAILED("DATA_INTEGRITY_VALIDATION_FAILED"),
    EMAIL_ALREADY_PRESENT("EMAIL_ALREADY_PRESENT"),
    PASSWORD_VALIDATION_FAILED("PASSWORD_VALIDATION_FAILED"),
    USERNAME_GENERATION_FAILED("USERNAME_GENERATION_FAILED"),
    UNHANDLED_EXCEPTION_OCCURRED("UNHANDLED_EXCEPTION_OCCURRED");


    private final String errorString;

    ErrorCode(String errorString) {
        this.errorString = errorString;
    }

    @Override
    public String toString() {
        return errorString;
    }
}

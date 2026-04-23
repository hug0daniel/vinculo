package com.vinculo.application.exception;

public class InactiveUserException extends RuntimeException {
    public InactiveUserException() {
        super("User account is inactive");
    }

    public InactiveUserException(String message) {
        super(message);
    }
}
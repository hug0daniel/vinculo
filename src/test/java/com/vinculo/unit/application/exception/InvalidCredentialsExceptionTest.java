package com.vinculo.unit.application.exception;

import com.vinculo.application.exception.InvalidCredentialsException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidCredentialsExceptionTest {

    @Test
    void shouldHaveDefaultMessage() {
        var ex = new InvalidCredentialsException();
        assertEquals("Invalid credentials", ex.getMessage());
    }
}
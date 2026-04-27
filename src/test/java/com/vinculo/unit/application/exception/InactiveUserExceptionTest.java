package com.vinculo.unit.application.exception;

import com.vinculo.application.exception.InactiveUserException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InactiveUserExceptionTest {

    @Test
    void shouldHaveDefaultMessage() {
        var ex = new InactiveUserException();
        assertEquals("User account is inactive", ex.getMessage());
    }
}
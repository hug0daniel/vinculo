package com.vinculo.unit.application.exception;

import com.vinculo.application.exception.ResourceNotFoundException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceNotFoundExceptionTest {

    @Test
    void shouldIncludeResourceIdInMessage() {
        var id = "test-id";
        var ex = new ResourceNotFoundException(id);
        assertTrue(ex.getMessage().contains(id));
    }
}
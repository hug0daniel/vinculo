package com.vinculo.unit.api.user.dto;

import com.vinculo.api.user.dto.CreateUserRequest;
import com.vinculo.api.user.dto.LoginRequest;
import com.vinculo.domain.user.model.Role;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("User DTO Validation")
class UserDtoValidationTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Nested
    @DisplayName("CreateUserRequest")
    class CreateUserRequestTests {

        @Test
        @DisplayName("should pass with valid data")
        void shouldPassWithValidData() {
            var request = new CreateUserRequest(
                "test@example.com",
                "password123",
                "testuser",
                Role.VOLUNTEER,
                null
            );

            var violations = validator.validate(request);

            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("should fail when email is blank")
        void shouldFailWhenEmailBlank() {
            var request = new CreateUserRequest(
                "",
                "password123",
                "testuser",
                Role.VOLUNTEER,
                null
            );

            var violations = validator.validate(request);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
        }

        @Test
        @DisplayName("should fail when email is invalid format")
        void shouldFailWhenEmailInvalid() {
            var request = new CreateUserRequest(
                "not-an-email",
                "password123",
                "testuser",
                Role.VOLUNTEER,
                null
            );

            var violations = validator.validate(request);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
        }

        @Test
        @DisplayName("should fail when password is too short")
        void shouldFailWhenPasswordTooShort() {
            var request = new CreateUserRequest(
                "test@example.com",
                "short",
                "testuser",
                Role.VOLUNTEER,
                null
            );

            var violations = validator.validate(request);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
        }

        @Test
        @DisplayName("should fail when userName is blank")
        void shouldFailWhenUserNameBlank() {
            var request = new CreateUserRequest(
                "test@example.com",
                "password123",
                "",
                Role.VOLUNTEER,
                null
            );

            var violations = validator.validate(request);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("userName")));
        }

        @Test
        @DisplayName("should fail when role is null")
        void shouldFailWhenRoleNull() {
            var request = new CreateUserRequest(
                "test@example.com",
                "password123",
                "testuser",
                null,
                null
            );

            var violations = validator.validate(request);

            assertFalse(violations.isEmpty());
            assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("role")));
        }
    }

    @Nested
    @DisplayName("LoginRequest")
    class LoginRequestTests {

        @Test
        @DisplayName("should pass with valid data")
        void shouldPassWithValidData() {
            var request = new LoginRequest("test@example.com", "password123");

            var violations = validator.validate(request);

            assertTrue(violations.isEmpty());
        }

        @Test
        @DisplayName("should fail when email is blank")
        void shouldFailWhenEmailBlank() {
            var request = new LoginRequest("", "password123");

            var violations = validator.validate(request);

            assertFalse(violations.isEmpty());
        }

        @Test
        @DisplayName("should fail when password is blank")
        void shouldFailWhenPasswordBlank() {
            var request = new LoginRequest("test@example.com", "");

            var violations = validator.validate(request);

            assertFalse(violations.isEmpty());
        }
    }
}
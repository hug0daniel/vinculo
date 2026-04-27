package com.vinculo.api.user.dto;

import com.vinculo.domain.user.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

@Schema(description = "User registration request")
public record CreateUserRequest(
    @NotBlank @Email
    @Schema(description = "User email", example = "user@example.com")
    String email,
    
    @NotBlank @Size(min = 8)
    @Schema(description = "User password", example = "password123")
    String password,
    
    @NotBlank @Size(max = 50)
    @Schema(description = "Username", example = "johndoe")
    String userName,
    
    @NotNull
    @Schema(description = "User role", example = "VOLUNTEER", allowableValues = {"VOLUNTEER", "WAREHOUSE_MANAGER", "ENTITY_USER", "ADMIN"})
    Role role,
    
    @Schema(description = "Partner UUID (optional)", example = "550e8400-e29b-41d4-a716-446655440000")
    UUID partnerId
) {}
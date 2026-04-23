package com.vinculo.api.user.dto;

import com.vinculo.domain.user.model.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record CreateUserRequest(
    @NotBlank @Email
    String email,
    @NotBlank @Size(min = 8)
    String password,
    @NotBlank @Size(max = 50)
    String userName,
    @NotNull
    Role role,
    UUID partnerId
) {}
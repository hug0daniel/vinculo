package com.vinculo.api.user.dto;

import java.util.UUID;

public record LoginResponse(
    String token,
    UUID userId,
    String email,
    String userName,
    String role
) {}
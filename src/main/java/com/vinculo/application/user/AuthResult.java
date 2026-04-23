package com.vinculo.application.user;

import java.util.UUID;

public record AuthResult(
    UUID userId,
    String email,
    String userName,
    String role,
    String token
) {}
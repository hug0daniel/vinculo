package com.vinculo.api.user.controller;

import java.util.UUID;

public record LoginResponse(
    String token,
    UUID userId,
    String email,
    String userName,
    String role
) {}
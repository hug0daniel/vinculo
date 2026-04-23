package com.vinculo.api.user.dto;

import com.vinculo.domain.user.model.Role;
import java.util.UUID;

public record CreateUserResponse(
    UUID userId,
    String email,
    String userName,
    Role role,
    PartnerSummary partner,
    boolean active
) {
    public record PartnerSummary(UUID id, String name) {}
}
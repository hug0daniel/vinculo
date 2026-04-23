package com.vinculo.api.user;

import com.vinculo.api.user.controller.LoginResponse;
import com.vinculo.api.user.dto.CreateUserResponse;
import com.vinculo.domain.user.model.AppUser;

public class UserMapper {

    public static CreateUserResponse toResponse(AppUser user) {
        var partnerSummary = user.getPartner() != null
            ? new CreateUserResponse.PartnerSummary(
                user.getPartner().getId(),
                user.getPartner().getOrganizationName())
            : null;

        return new CreateUserResponse(
            user.getId(),
            user.getEmail(),
            user.getUserName(),
            user.getRole(),
            partnerSummary,
            user.isActive()
        );
    }

    public static LoginResponse toLoginResponse(AppUser user, String token) {
        return new LoginResponse(
            token,
            user.getId(),
            user.getEmail(),
            user.getUserName(),
            user.getRole().name()
        );
    }
}
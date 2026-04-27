package com.vinculo.api.user.utils;

import com.vinculo.api.user.dto.LoginResponse;
import com.vinculo.api.user.dto.CreateUserResponse;
import com.vinculo.application.user.AuthResult;
import com.vinculo.domain.user.model.User;

public class UserMapper {

    public static CreateUserResponse toResponse(User user) {
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

    public static LoginResponse toLoginResponse(AuthResult result) {
        return new LoginResponse(
            result.token(),
            result.userId(),
            result.email(),
            result.userName(),
            result.role()
        );
    }
}
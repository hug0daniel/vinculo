package com.vinculo.application.user;

import com.vinculo.api.user.dto.LoginRequest;

public interface AuthService {

    AuthResult login(LoginRequest request);
}
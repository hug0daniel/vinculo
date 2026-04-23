package com.vinculo.api.user.controller;

import com.vinculo.api.user.dto.LoginRequest;
import com.vinculo.application.user.AuthResult;
import com.vinculo.application.user.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "User management and authentication")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResult result = authService.login(request);
        LoginResponse response = new LoginResponse(
            result.token(),
            result.userId(),
            result.email(),
            result.userName(),
            result.role()
        );
        return ResponseEntity.ok(response);
    }
}
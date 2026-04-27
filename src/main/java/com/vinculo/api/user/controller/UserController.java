package com.vinculo.api.user.controller;

import com.vinculo.api.user.dto.CreateUserRequest;
import com.vinculo.api.user.dto.CreateUserResponse;
import com.vinculo.api.user.dto.LoginRequest;
import com.vinculo.api.user.dto.LoginResponse;
import com.vinculo.api.user.utils.UserMapper;
import com.vinculo.application.user.AuthService;
import com.vinculo.application.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "User management & Authentication")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/register")
    @Operation(summary = "Create a new user", description = "Creates a new user (volunteer, warehouse manager, entity user, or admin)")
    public ResponseEntity<CreateUserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreateUserResponse> getUser(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate and receive JWT token")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        var result = authService.login(request);
        return ResponseEntity.ok(UserMapper.toLoginResponse(result));
    }
}
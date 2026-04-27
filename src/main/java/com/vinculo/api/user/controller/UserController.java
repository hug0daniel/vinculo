package com.vinculo.api.user.controller;

import com.vinculo.api.user.dto.CreateUserRequest;
import com.vinculo.api.user.dto.CreateUserResponse;
import com.vinculo.api.user.dto.LoginRequest;
import com.vinculo.api.user.dto.LoginResponse;
import com.vinculo.api.user.utils.UserMapper;
import com.vinculo.application.user.AuthService;
import com.vinculo.application.user.UserService;
import com.vinculo.domain.user.model.Partner;
import com.vinculo.domain.user.repository.PartnerRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "User management & Authentication")
public class UserController {

    private final UserService userService;
    private final AuthService authService;
    private final PartnerRepository partnerRepository;

    public UserController(UserService userService, AuthService authService, PartnerRepository partnerRepository) {
        this.userService = userService;
        this.authService = authService;
        this.partnerRepository = partnerRepository;
    }

    @PostMapping("/register")
    @Operation(summary = "Register new user (public)", description = "Anyone can register")
    public ResponseEntity<CreateUserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<CreateUserResponse> getUser(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate and receive JWT token")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        var result = authService.login(request);
        return ResponseEntity.ok(UserMapper.toLoginResponse(result));
    }

    @GetMapping("/debug/partners")
    @Operation(summary = "Debug: List all partners")
    public ResponseEntity<List<Partner>> getPartners() {
        return ResponseEntity.ok(partnerRepository.findAll());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users (ADMIN only)")
    public ResponseEntity<List<CreateUserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}
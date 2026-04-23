package com.vinculo.api.user.controller;

import com.vinculo.api.user.dto.CreateUserRequest;
import com.vinculo.api.user.dto.CreateUserResponse;
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
@Tag(name = "Users", description = "User management")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Create a new user", description = "Creates a new user (volunteer, warehouse manager, entity user, or admin)")
    public ResponseEntity<CreateUserResponse> createUser(
            @Valid @RequestBody CreateUserRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CreateUserResponse> getUser(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUser(id));
    }
}
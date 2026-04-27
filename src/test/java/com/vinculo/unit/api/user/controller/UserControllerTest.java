package com.vinculo.unit.api.user.controller;

import com.vinculo.api.user.controller.UserController;
import com.vinculo.api.user.dto.CreateUserRequest;
import com.vinculo.api.user.dto.CreateUserResponse;
import com.vinculo.api.user.dto.LoginRequest;
import com.vinculo.application.user.AuthResult;
import com.vinculo.application.user.AuthService;
import com.vinculo.application.user.UserService;
import com.vinculo.domain.user.model.Partner;
import com.vinculo.domain.user.model.PartnerType;
import com.vinculo.domain.user.model.Role;
import com.vinculo.domain.user.repository.PartnerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthService authService;

    @Mock
    private PartnerRepository partnerRepository;

    private UserController userController;
    private CreateUserResponse userResponse;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userController = new UserController(userService, authService, partnerRepository);
        userId = UUID.randomUUID();
        userResponse = new CreateUserResponse(
            userId,
            "test@test.com",
            "testuser",
            Role.VOLUNTEER,
            null,
            true
        );
    }

    @Test
    void shouldRegisterUser() {
        var request = new CreateUserRequest(
            "new@test.com",
            "password123",
            "newuser",
            Role.VOLUNTEER,
            null
        );

        when(userService.createUser(any(CreateUserRequest.class))).thenReturn(userResponse);

        var result = userController.createUser(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        verify(userService, times(1)).createUser(any(CreateUserRequest.class));
    }

    @Test
    void shouldGetUserById() {
        when(userService.getUser(userId)).thenReturn(userResponse);

        var result = userController.getUser(userId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(userId, result.getBody().userId());
        verify(userService, times(1)).getUser(userId);
    }

    @Test
    void shouldLoginUser() {
        var loginRequest = new LoginRequest("test@test.com", "password123");
        var authResult = new AuthResult(
            userId,
            "test@test.com",
            "testuser",
            "VOLUNTEER",
            "token123"
        );

        when(authService.login(any(LoginRequest.class))).thenReturn(authResult);

        var result = userController.login(loginRequest);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertNotNull(result.getBody());
        verify(authService, times(1)).login(any(LoginRequest.class));
    }

    @Test
    void shouldGetPartners() {
        var partner = new Partner("Test Org", PartnerType.NGO, "contact@test.com", "Lisbon");
        
        when(partnerRepository.findAll()).thenReturn(List.of(partner));

        var result = userController.getPartners();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(1, result.getBody().size());
        verify(partnerRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnUserResponseWithCorrectData() {
        when(userService.getUser(userId)).thenReturn(userResponse);

        var result = userController.getUser(userId);
        var body = result.getBody();

        assertNotNull(body);
        assertEquals("test@test.com", body.email());
        assertEquals("testuser", body.userName());
        assertEquals(Role.VOLUNTEER, body.role());
        assertTrue(body.active());
    }
}
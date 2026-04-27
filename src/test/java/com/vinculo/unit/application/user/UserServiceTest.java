package com.vinculo.unit.application.user;

import com.vinculo.api.user.dto.CreateUserRequest;
import com.vinculo.application.exception.ResourceNotFoundException;
import com.vinculo.domain.user.model.Partner;
import com.vinculo.domain.user.model.PartnerType;
import com.vinculo.domain.user.model.Role;
import com.vinculo.domain.user.model.User;
import com.vinculo.domain.user.repository.PartnerRepository;
import com.vinculo.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PartnerRepository partnerRepository;

    private com.vinculo.application.user.UserService userService;

    private UUID userId;
    private UUID partnerId;

    @BeforeEach
    void setUp() {
        userService = new com.vinculo.application.user.UserService(userRepository, partnerRepository);
        userId = UUID.randomUUID();
        partnerId = UUID.randomUUID();
    }

    @Test
    void shouldCreateUser() {
        var request = new CreateUserRequest(
            "new@test.com",
            "password123",
            "newuser",
            Role.VOLUNTEER,
            null
        );

        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0, User.class));

        var result = userService.createUser(request);

        assertNotNull(result);
        assertEquals("new@test.com", result.email());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldCreateUserWithPartner() {
        var request = new CreateUserRequest(
            "new@test.com",
            "password123",
            "newuser",
            Role.VOLUNTEER,
            partnerId
        );

        var partner = new Partner("Test Org", PartnerType.NGO, "contact@test.com", "Lisbon");

        when(partnerRepository.findById(partnerId)).thenReturn(Optional.of(partner));
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        var result = userService.createUser(request);

        assertNotNull(result);
        assertNotNull(result.partner());
        verify(partnerRepository, times(1)).findById(partnerId);
    }

    @Test
    void shouldFailWhenPartnerNotFound() {
        var request = new CreateUserRequest(
            "new@test.com",
            "password123",
            "newuser",
            Role.VOLUNTEER,
            partnerId
        );

        when(partnerRepository.findById(partnerId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> 
            userService.createUser(request)
        );
    }

    @Test
    void shouldGetUserById() {
        var user = User.builder()
            .email("test@test.com")
            .userName("testuser")
            .role(Role.VOLUNTEER)
            .active(true)
            .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        var result = userService.getUser(userId);

        assertNotNull(result);
        assertEquals("test@test.com", result.email());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void shouldFailWhenUserNotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> 
            userService.getUser(userId)
        );
    }

    @Test
    void shouldGetAllUsers() {
        var user = User.builder()
            .email("test@test.com")
            .userName("testuser")
            .role(Role.VOLUNTEER)
            .active(true)
            .build();

        when(userRepository.findAll()).thenReturn(List.of(user));

        var result = userService.getAllUsers();

        assertEquals(1, result.size());
        verify(userRepository, times(1)).findAll();
    }
}
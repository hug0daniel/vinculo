package com.vinculo.unit.application.user;

import com.vinculo.api.user.dto.LoginRequest;
import com.vinculo.application.exception.InactiveUserException;
import com.vinculo.application.exception.InvalidCredentialsException;
import com.vinculo.application.user.AuthServiceImpl;
import com.vinculo.domain.user.model.Role;
import com.vinculo.domain.user.model.User;
import com.vinculo.domain.user.repository.UserRepository;
import com.vinculo.infrastructure.security.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        authService = new AuthServiceImpl(userRepository, passwordEncoder, jwtProvider);
    }

    @Test
    void shouldLoginSuccessfully() {
        var loginRequest = new LoginRequest("test@test.com", "password123");
        var user = createActiveUser();
        
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);
        when(jwtProvider.generateToken(any(), any(), any())).thenReturn("jwt-token");

        var result = authService.login(loginRequest);

        assertNotNull(result);
        assertEquals("jwt-token", result.token());
        verify(userRepository, times(1)).findByEmail("test@test.com");
    }

    @Test
    void shouldFailWithInvalidCredentialsWhenUserNotFound() {
        var loginRequest = new LoginRequest("notfound@test.com", "password123");
        
        when(userRepository.findByEmail("notfound@test.com")).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> 
            authService.login(loginRequest)
        );
    }

    @Test
    void shouldFailWithInvalidCredentialsWhenPasswordWrong() {
        var loginRequest = new LoginRequest("test@test.com", "wrongpassword");
        var user = createActiveUser();
        
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", "hashedPassword")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> 
            authService.login(loginRequest)
        );
    }

    @Test
    void shouldFailWhenUserIsInactive() {
        var loginRequest = new LoginRequest("test@test.com", "password123");
        var user = User.builder()
            .email("test@test.com")
            .password("hashedPassword")
            .userName("testuser")
            .role(Role.VOLUNTEER)
            .active(false)
            .build();
        
        when(userRepository.findByEmail("test@test.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);

        assertThrows(InactiveUserException.class, () -> 
            authService.login(loginRequest)
        );
    }

    private User createActiveUser() {
        return User.builder()
            .email("test@test.com")
            .password("hashedPassword")
            .userName("testuser")
            .role(Role.VOLUNTEER)
            .active(true)
            .build();
    }
}
package com.vinculo.application.user;

import com.vinculo.api.user.dto.LoginRequest;
import com.vinculo.application.exception.InactiveUserException;
import com.vinculo.application.exception.InvalidCredentialsException;
import com.vinculo.domain.user.model.User;
import com.vinculo.domain.user.repository.UserRepository;
import com.vinculo.infrastructure.security.JwtProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public AuthResult login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
            .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        if (!user.isActive()) {
            throw new InactiveUserException();
        }

        String token = jwtProvider.generateToken(
            user.getId(),
            user.getEmail(),
            user.getRole().name()
        );

        return new AuthResult(
            user.getId(),
            user.getEmail(),
            user.getUserName(),
            user.getRole().name(),
            token
        );
    }
}
package com.vinculo.application.user;

import com.vinculo.api.user.dto.CreateUserRequest;
import com.vinculo.api.user.dto.CreateUserResponse;
import com.vinculo.api.user.UserMapper;
import com.vinculo.application.exception.ResourceNotFoundException;
import com.vinculo.domain.user.model.AppUser;
import com.vinculo.domain.user.model.Partner;
import com.vinculo.domain.user.repository.PartnerRepository;
import com.vinculo.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final PartnerRepository partnerRepository;

    public UserService(UserRepository userRepository, PartnerRepository partnerRepository) {
        this.userRepository = userRepository;
        this.partnerRepository = partnerRepository;
    }

    public CreateUserResponse createUser(CreateUserRequest request) {
        Partner partner = null;
        if (request.partnerId() != null) {
            partner = partnerRepository.findById(request.partnerId())
                .orElseThrow(() -> new ResourceNotFoundException("Partner not found: " + request.partnerId()));
        }

        var user = AppUser.builder()
            .email(request.email())
            .password(request.password())
            .userName(request.userName())
            .role(request.role())
            .partner(partner)
            .active(true)
            .build();

        userRepository.save(user);

        return UserMapper.toResponse(user);
    }

    public CreateUserResponse getUser(UUID id) {
        var user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        return UserMapper.toResponse(user);
    }
}
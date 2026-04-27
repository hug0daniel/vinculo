package com.vinculo.application.user;

import com.vinculo.api.user.dto.CreateUserRequest;
import com.vinculo.api.user.dto.CreateUserResponse;
import com.vinculo.api.user.utils.UserMapper;
import com.vinculo.application.exception.ResourceNotFoundException;
import com.vinculo.domain.user.model.Partner;
import com.vinculo.domain.user.model.User;
import com.vinculo.domain.user.repository.PartnerRepository;
import com.vinculo.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PartnerRepository partnerRepository;

    public UserService(UserRepository userRepository, PartnerRepository partnerRepository) {
        this.userRepository = userRepository;
        this.partnerRepository = partnerRepository;
    }

    @Transactional
    public CreateUserResponse createUser(final CreateUserRequest request) {
        Partner partner = null;
        if (request.partnerId() != null) {
            partner = partnerRepository.findById(request.partnerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Partner not found: " + request.partnerId()));
        }

        final User user = User.builder()
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

    @Transactional(readOnly = true)
    public CreateUserResponse getUser(final UUID id) {
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        return UserMapper.toResponse(user);
    }

    @Transactional(readOnly = true)
    public List<CreateUserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserMapper::toResponse)
                .toList();
    }
}
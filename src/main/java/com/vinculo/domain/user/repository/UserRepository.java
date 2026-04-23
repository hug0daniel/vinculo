package com.vinculo.domain.user.repository;

import com.vinculo.domain.user.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UserRepository extends JpaRepository<AppUser, UUID> {
    java.util.Optional<AppUser> findByEmail(String email);
}

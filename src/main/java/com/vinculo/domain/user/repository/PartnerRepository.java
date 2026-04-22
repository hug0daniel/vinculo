package com.vinculo.domain.user.repository;

import com.vinculo.domain.user.model.Partner;
import com.vinculo.domain.user.model.PartnerType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface PartnerRepository extends JpaRepository<Partner, UUID> {
    List<Partner> findByType(PartnerType type);
}
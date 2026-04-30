package com.vinculo.domain.disaster.repository;

import com.vinculo.domain.disaster.model.Disaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DisasterRepository extends JpaRepository<Disaster, UUID> {
}

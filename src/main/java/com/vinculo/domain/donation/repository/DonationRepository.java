package com.vinculo.domain.donation.repository;

import com.vinculo.domain.donation.model.DonationOffer;
import com.vinculo.domain.donation.model.DonationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DonationRepository extends JpaRepository<DonationOffer, UUID> {

    List<DonationOffer> findByStatus(DonationStatus status);

    List<DonationOffer> findByWarehouseId(UUID warehouseId);
}
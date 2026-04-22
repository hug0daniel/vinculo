package com.vinculo.application.donation;

import com.vinculo.api.donation.CreateDonationResponse;
import com.vinculo.domain.donation.model.*;
import com.vinculo.domain.donation.repository.DonationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class DonationService {

    private final DonationRepository donationRepository;

    public DonationService(DonationRepository donationRepository) {
        this.donationRepository = donationRepository;
    }

    // TODO: Logic will be implemented when Inventory domain layer implemented
    public CreateDonationResponse createDonation(CreateDonationCommand command) {
        return null;
    }
    // TODO: Logic will be implemented when Inventory domain layer implemented
    public CreateDonationResponse acceptDonation(UUID donationId, UUID warehouseId) {
        return null;
    }

    // TODO: Logic will be implemented when Inventory domain layer implemented
    public CreateDonationResponse rejectDonation(UUID donationId) {
        return null;
    }

    private CreateDonationResponse toResponse(DonationOffer donation) {
        var donorDto = new CreateDonationResponse.DonorDto(
            donation.getDonor().name(),
            donation.getDonor().contact(),
            donation.getDonor().type().name()
        );

        var itemsDto = donation.getItems().stream()
            .map(item -> new CreateDonationResponse.ItemDto(
                item.getProductId(),
                item.getQuantity(),
                item.getUnit(),
                item.getExpiryDate()
            ))
            .toList();

        return new CreateDonationResponse(
            donation.getId(),
            donation.getStatus(),
            donation.getCreatedAt(),
            donorDto,
            itemsDto
        );
    }
}
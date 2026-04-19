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

    public CreateDonationResponse createDonation(CreateDonationCommand command) {
        var donor = new Donor(
            command.donor().name(),
            command.donor().contact(),
            command.donor().type()
        );

        var donation = new DonationOffer(donor);

        for (var item : command.items()) {
            var donationItem = new DonationItem(
                item.productId(),
                item.quantity(),
                item.unit(),
                item.expiryDate()
            );
            donation.addItem(donationItem);
        }

        donationRepository.save(donation);

        return toResponse(donation);
    }

    public CreateDonationResponse acceptDonation(UUID donationId, UUID warehouseId) {
        var donation = donationRepository.findById(donationId)
            .orElseThrow(() -> new IllegalArgumentException("Donation not found: " + donationId));
        
        donation.accept(warehouseId);
        
        return toResponse(donation);
    }

    public CreateDonationResponse rejectDonation(UUID donationId) {
        var donation = donationRepository.findById(donationId)
            .orElseThrow(() -> new IllegalArgumentException("Donation not found: " + donationId));
        
        donation.reject();
        
        return toResponse(donation);
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
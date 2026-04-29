package com.vinculo.application.donation;

import com.vinculo.api.donation.dto.DonationRequest;
import com.vinculo.api.donation.dto.DonationResponse;
import com.vinculo.domain.donation.model.DonationStatus;

import java.util.List;
import java.util.UUID;

public interface DonationService {

    DonationResponse createDonation(DonationRequest request);

    DonationResponse acceptDonation(UUID donationId, UUID warehouseId);

    DonationResponse rejectDonation(UUID donationId);

    DonationResponse getDonation(UUID id);

    List<DonationResponse> getDonations(DonationStatus status);
}
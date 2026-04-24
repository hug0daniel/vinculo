package com.vinculo.application.donation;

import com.vinculo.api.donation.DonationRequest;
import com.vinculo.api.donation.DonationResponse;

import java.util.UUID;

public interface DonationService {

    DonationResponse createDonation(DonationRequest request);

    DonationResponse acceptDonation(UUID donationId, UUID warehouseId);

    DonationResponse rejectDonation(UUID donationId);
}
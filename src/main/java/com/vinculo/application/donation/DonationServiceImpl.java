package com.vinculo.application.donation;

import com.vinculo.api.donation.dto.DonationRequest;
import com.vinculo.api.donation.dto.DonationResponse;
import com.vinculo.api.donation.utils.DonationMapper;
import com.vinculo.application.exception.ResourceNotFoundException;
import com.vinculo.domain.donation.model.DonationOffer;
import com.vinculo.domain.donation.repository.DonationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class DonationServiceImpl implements DonationService {

    private final DonationRepository donationRepository;
    private final DonationMapper mapper;

    public DonationServiceImpl(DonationRepository donationRepository, DonationMapper mapper) {
        this.donationRepository = donationRepository;
        this.mapper = mapper;
    }

    @Override
    public DonationResponse createDonation(DonationRequest request) {
        DonationOffer donation = mapper.toEntity(request);
        DonationOffer saved = donationRepository.save(donation);
        return mapper.toResponse(saved);
    }

    @Override
    public DonationResponse acceptDonation(UUID donationId, UUID warehouseId) {
        DonationOffer donation = donationRepository.findById(donationId)
            .orElseThrow(() -> new ResourceNotFoundException("Donation not found"));

        donation.accept(warehouseId);
        return mapper.toResponse(donation);
    }

    @Override
    public DonationResponse rejectDonation(UUID donationId) {
        DonationOffer donation = donationRepository.findById(donationId)
            .orElseThrow(() -> new ResourceNotFoundException("Donation not found"));

        // TODO: When donation gets rejecte
        donation.reject();
        return mapper.toResponse(donation);
    }
}
package com.vinculo.unit.application.donation;

import com.vinculo.api.donation.dto.DonationRequest;
import com.vinculo.api.donation.dto.DonationRequest.DonorDto;
import com.vinculo.api.donation.dto.DonationRequest.DonationItemDto;
import com.vinculo.api.donation.utils.DonationMapper;
import com.vinculo.application.donation.DonationServiceImpl;
import com.vinculo.application.exception.ResourceNotFoundException;
import com.vinculo.domain.donation.model.*;
import com.vinculo.domain.donation.repository.DonationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DonationServiceTest {

    @Mock
    private DonationRepository donationRepository;

    private DonationServiceImpl donationService;
    private UUID donationId;
    private UUID warehouseId;

    @BeforeEach
    void setUp() {
        DonationMapper mapper = new DonationMapper();
        donationService = new DonationServiceImpl(donationRepository, mapper);
        donationId = UUID.randomUUID();
        warehouseId = UUID.randomUUID();
    }

    @Nested
    @DisplayName("Create Donation")
    class CreateDonationTests {

        @Test
        @DisplayName("should create donation with PENDING status")
        void shouldCreateDonationWithPendingStatus() {
            when(donationRepository.save(any(DonationOffer.class)))
                .thenAnswer(inv -> inv.getArgument(0));

            var response = donationService.createDonation(createDonationRequest());

            assertNotNull(response);
            assertEquals(DonationStatus.PENDING, response.status());
            assertNotNull(response.donor());
            assertEquals("John Doe", response.donor().name());
        }
    }

    @Nested
    @DisplayName("Accept Donation")
    class AcceptDonationTests {

        @Test
        @DisplayName("should accept pending donation and assign warehouse")
        void shouldAcceptPendingDonation() {
            var donation = createPendingDonation();
            when(donationRepository.findById(donationId)).thenReturn(Optional.of(donation));

            var response = donationService.acceptDonation(donationId, warehouseId);

            assertEquals(DonationStatus.ACCEPTED, response.status());
            assertEquals(warehouseId, donation.getWarehouseId());
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException when donation not found")
        void shouldThrowWhenDonationNotFoundOnAccept() {
            when(donationRepository.findById(donationId)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () ->
                donationService.acceptDonation(donationId, warehouseId)
            );
        }
    }

    @Nested
    @DisplayName("Reject Donation")
    class RejectDonationTests {

        @Test
        @DisplayName("should reject pending donation")
        void shouldRejectPendingDonation() {
            var donation = createPendingDonation();
            when(donationRepository.findById(donationId)).thenReturn(Optional.of(donation));

            var response = donationService.rejectDonation(donationId);

            assertEquals(DonationStatus.REJECTED, response.status());
            assertEquals(DonationStatus.REJECTED, donation.getStatus());
        }

        @Test
        @DisplayName("should throw ResourceNotFoundException when donation not found")
        void shouldThrowWhenDonationNotFoundOnReject() {
            when(donationRepository.findById(donationId)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () ->
                donationService.rejectDonation(donationId)
            );
        }
    }

    private DonationRequest createDonationRequest() {
        var donor = new DonorDto("John Doe", "john@example.com", DonorType.INDIVIDUAL);
        var item = new DonationItemDto(UUID.randomUUID(), new BigDecimal("50"), QuantityUnit.KG, LocalDate.now().plusMonths(6));
        return new DonationRequest(donor, List.of(item));
    }

    private DonationOffer createPendingDonation() {
        var donor = new Donor("John Doe", "john@example.com", DonorType.INDIVIDUAL);
        var donation = DonationOffer.builder()
            .donor(donor)
            .build();
        donation.addItem(UUID.randomUUID(), new BigDecimal("50"), QuantityUnit.KG, LocalDate.now().plusMonths(6));
        return donation;
    }
}
package com.vinculo.application.donation;

import com.vinculo.api.donation.dto.DonationRequest;
import com.vinculo.api.donation.utils.DonationMapper;
import com.vinculo.domain.donation.model.DonationOffer;
import com.vinculo.domain.donation.model.DonationStatus;
import com.vinculo.domain.donation.model.DonorType;
import com.vinculo.domain.donation.model.QuantityUnit;
import com.vinculo.domain.donation.repository.DonationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DonationServiceTest {

    @Mock
    private DonationRepository donationRepository;

    private DonationServiceImpl donationService;
    private DonationRequest validRequest;
    private UUID productId;

    @BeforeEach
    void setUp() {
        DonationMapper mapper = new DonationMapper();
        donationService = new DonationServiceImpl(donationRepository, mapper);

        productId = UUID.randomUUID();

        var donor = new DonationRequest.DonorDto("John Doe", "john@example.com", DonorType.INDIVIDUAL);
        var item = new DonationRequest.DonationItemDto(productId, new BigDecimal("50"), QuantityUnit.KG, LocalDate.now().plusMonths(6));
        validRequest = new DonationRequest(donor, List.of(item));
    }

    @Test
    void shouldCreateDonation() {
        when(donationRepository.save(any(DonationOffer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0, DonationOffer.class));

        var response = donationService.createDonation(validRequest);

        assertNotNull(response);
        assertEquals(DonationStatus.PENDING, response.status());
        assertEquals("John Doe", response.donor().name());
        assertEquals(1, response.items().size());
        verify(donationRepository, times(1)).save(any(DonationOffer.class));
    }

    @Test
    void shouldMapDonorTypeCorrectly() {
        when(donationRepository.save(any(DonationOffer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var donor = new DonationRequest.DonorDto("Company", "contact@company.com", DonorType.COMPANY);
        var request = new DonationRequest(donor, List.of());

        var response = donationService.createDonation(request);

        assertEquals("COMPANY", response.donor().type());
    }

    @Test
    void shouldMapQuantityUnitCorrectly() {
        when(donationRepository.save(any(DonationOffer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var donor = new DonationRequest.DonorDto("Test", "test@test.com", DonorType.INDIVIDUAL);
        var item = new DonationRequest.DonationItemDto(productId, new BigDecimal("100"), QuantityUnit.LITER, null);
        var request = new DonationRequest(donor, List.of(item));

        var response = donationService.createDonation(request);

        assertEquals(QuantityUnit.LITER, response.items().getFirst().unit());
    }
}
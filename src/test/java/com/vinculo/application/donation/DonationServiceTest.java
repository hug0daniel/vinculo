package com.vinculo.application.donation;

import com.vinculo.domain.donation.model.*;
import com.vinculo.domain.donation.repository.DonationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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

    @InjectMocks
    private DonationService donationService;

    private CreateDonationCommand validCommand;
    private UUID productId;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();

        var donor = new Donor("John Doe", "john@example.com", DonorType.INDIVIDUAL);
        var item = new CreateDonationCommand.Item(productId, new BigDecimal("50"), QuantityUnit.KG, LocalDate.now().plusMonths(6));
        validCommand = new CreateDonationCommand(donor, List.of(item));
    }

    @Test
    void shouldCreateDonation() {
        when(donationRepository.save(any(DonationOffer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0, DonationOffer.class));

        var response = donationService.createDonation(validCommand);

        assertNotNull(response);
        assertEquals(DonationStatus.PENDING, response.status());
        assertEquals("John Doe", response.donor().name());
        assertEquals(1, response.items().size());
        verify(donationRepository, times(1)).save(any(DonationOffer.class));
    }

    @Test
    void shouldMapDonorTypeCorrectly() {
        when(donationRepository.save(any(DonationOffer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var donor = new Donor("Company", "contact@company.com", DonorType.COMPANY);
        var command = new CreateDonationCommand(donor, List.of());
        
        var response = donationService.createDonation(command);

        assertEquals("COMPANY", response.donor().type());
    }

    @Test
    void shouldMapQuantityUnitCorrectly() {
        when(donationRepository.save(any(DonationOffer.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var donor = new Donor("Test", "test@test.com", DonorType.INDIVIDUAL);
        var item = new CreateDonationCommand.Item(productId, new BigDecimal("100"), QuantityUnit.LITER, null);
        var command = new CreateDonationCommand(donor, List.of(item));
        
        var response = donationService.createDonation(command);

        assertEquals(QuantityUnit.LITER, response.items().getFirst().unit());
    }
}
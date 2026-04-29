package com.vinculo.unit.api.donation.controller;

import com.vinculo.api.donation.dto.DonationRequest;
import com.vinculo.api.donation.dto.DonationRequest.DonorDto;
import com.vinculo.api.donation.dto.DonationRequest.DonationItemDto;
import com.vinculo.api.donation.dto.DonationResponse;
import com.vinculo.application.donation.DonationService;
import com.vinculo.domain.donation.model.DonationStatus;
import com.vinculo.domain.donation.model.DonorType;
import com.vinculo.domain.donation.model.QuantityUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DonationControllerTest {

    @Mock
    private DonationService donationService;

    private com.vinculo.api.donation.controller.DonationController donationController;

    private UUID donationId;

    @BeforeEach
    void setUp() {
        donationController = new com.vinculo.api.donation.controller.DonationController(donationService);
        donationId = UUID.randomUUID();
    }

    @Test
    void shouldCreateDonation() {
        var donationResponse = createDonationResponse();
        
        when(donationService.createDonation(any(DonationRequest.class))).thenReturn(donationResponse);

        var request = createDonationRequest();
        var result = donationController.createDonation(request);

        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertNotNull(result.getBody());
        verify(donationService, times(1)).createDonation(any(DonationRequest.class));
    }

    @Test
    void shouldAcceptDonation() {
        var donationResponse = createDonationResponse();
        var warehouseId = UUID.randomUUID();
        
        when(donationService.acceptDonation(donationId, warehouseId)).thenReturn(donationResponse);

        var result = donationController.acceptDonation(donationId, warehouseId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(donationService, times(1)).acceptDonation(donationId, warehouseId);
    }

    @Test
    void shouldRejectDonation() {
        var donationResponse = createDonationResponse();
        
        when(donationService.rejectDonation(donationId)).thenReturn(donationResponse);

        var result = donationController.rejectDonation(donationId);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        verify(donationService, times(1)).rejectDonation(donationId);
    }

    private DonationRequest createDonationRequest() {
        var donor = new DonorDto("John Doe", "john@test.com", DonorType.INDIVIDUAL);
        var item = new DonationItemDto("Rice", new BigDecimal("50"), QuantityUnit.KG, LocalDate.now().plusMonths(6));
        return new DonationRequest(donor, List.of(item));
    }

    private DonationResponse createDonationResponse() {
        var donor = new DonationResponse.DonorDto("John Doe", "john@test.com", "INDIVIDUAL");
        var item = new DonationResponse.ItemDto("Rice", new BigDecimal("50"), null, null);
        return new DonationResponse(donationId, DonationStatus.PENDING, null, donor, List.of(item));
    }
}
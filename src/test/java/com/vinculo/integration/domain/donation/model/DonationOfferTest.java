package com.vinculo.integration.domain.donation.model;

import com.vinculo.domain.donation.model.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DonationOfferTest {

    @Test
    void shouldCreateDonationWithPendingStatus() {
        var donor = new Donor("John Doe", "john@example.com", DonorType.INDIVIDUAL);
        var donation = DonationOffer.builder()
            .donor(donor)
            .build();

        assertNull(donation.getId());
        assertEquals(DonationStatus.PENDING, donation.getStatus());
        assertNotNull(donation.getCreatedAt());
        assertEquals(donor, donation.getDonor());
        assertTrue(donation.getItems().isEmpty());
    }

    @Test
    void shouldAcceptDonation() {
        var donor = new Donor("John Doe", "john@example.com", DonorType.INDIVIDUAL);
        var donation = DonationOffer.builder()
            .donor(donor)
            .build();
        var warehouseId = UUID.randomUUID();

        donation.accept(warehouseId);

        assertEquals(DonationStatus.ACCEPTED, donation.getStatus());
        assertEquals(warehouseId, donation.getWarehouseId());
    }

    @Test
    void shouldRejectDonation() {
        var donor = new Donor("John Doe", "john@example.com", DonorType.INDIVIDUAL);
        var donation = DonationOffer.builder()
            .donor(donor)
            .build();

        donation.reject();

        assertEquals(DonationStatus.REJECTED, donation.getStatus());
    }

    @Test
    void shouldRedirectDonation() {
        var donor = new Donor("John Doe", "john@example.com", DonorType.INDIVIDUAL);
        var donation = DonationOffer.builder()
            .donor(donor)
            .build();

        donation.redirect();

        assertEquals(DonationStatus.REDIRECTED, donation.getStatus());
    }

    @Test
    void shouldAddItemsToDonation() {
        var donor = new Donor("John Doe", "john@example.com", DonorType.INDIVIDUAL);
        var donation = DonationOffer.builder()
            .donor(donor)
            .build();

        donation.addItem(UUID.randomUUID(), new BigDecimal("50"), QuantityUnit.KG, LocalDate.now().plusMonths(6));

        assertEquals(1, donation.getItems().size());
    }

    @Test
    void shouldFailToAcceptNonPendingDonation() {
        var donor = new Donor("John Doe", "john@example.com", DonorType.INDIVIDUAL);
        var donation = DonationOffer.builder()
            .donor(donor)
            .build();
        var warehouseId = UUID.randomUUID();
        donation.accept(warehouseId);

        assertThrows(IllegalStateException.class, () -> donation.accept(UUID.randomUUID()));
    }

    @Test
    void shouldFailWhenDonorIsMissing() {
        assertThrows(IllegalArgumentException.class, () -> 
            DonationOffer.builder().build()
        );
    }
}
package com.vinculo.domain.donation.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DonationOfferTest {

    @Test
    void shouldCreateDonationWithPendingStatus() {
        var donor = new Donor("John Doe", "john@example.com", DonorType.INDIVIDUAL);
        var donation = new DonationOffer(donor);

        assertNull(donation.getId());
        assertEquals(DonationStatus.PENDING, donation.getStatus());
        assertNotNull(donation.getCreatedAt());
        assertEquals(donor, donation.getDonor());
        assertTrue(donation.getItems().isEmpty());
    }

    @Test
    void shouldAcceptDonation() {
        var donor = new Donor("John Doe", "john@example.com", DonorType.INDIVIDUAL);
        var donation = new DonationOffer(donor);
        var warehouseId = UUID.randomUUID();

        donation.accept(warehouseId);

        assertEquals(DonationStatus.ACCEPTED, donation.getStatus());
        assertEquals(warehouseId, donation.getWarehouseId());
    }

    @Test
    void shouldRejectDonation() {
        var donor = new Donor("John Doe", "john@example.com", DonorType.INDIVIDUAL);
        var donation = new DonationOffer(donor);

        donation.reject();

        assertEquals(DonationStatus.REJECTED, donation.getStatus());
    }

    @Test
    void shouldRedirectDonation() {
        var donor = new Donor("John Doe", "john@example.com", DonorType.INDIVIDUAL);
        var donation = new DonationOffer(donor);

        donation.redirect();

        assertEquals(DonationStatus.REDIRECTED, donation.getStatus());
    }

    @Test
    void shouldAddItemsToDonation() {
        var donor = new Donor("John Doe", "john@example.com", DonorType.INDIVIDUAL);
        var donation = new DonationOffer(donor);
        var item = new DonationItem(UUID.randomUUID(), new BigDecimal("50"), QuantityUnit.KG, LocalDate.now().plusMonths(6));

        donation.addItem(item);

        assertEquals(1, donation.getItems().size());
        assertEquals(donation, item.getDonation());
    }

    @Test
    void shouldAllowAnonymousDonor() {
        var donor = new Donor(null, null, DonorType.INDIVIDUAL);
        var donation = new DonationOffer(donor);

        assertNull(donation.getDonor().name());
        assertNull(donation.getDonor().contact());
    }
}
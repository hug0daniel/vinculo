package com.vinculo.domain.donation;

import com.vinculo.domain.donation.model.*;
import com.vinculo.domain.donation.repository.DonationRepository;
import com.vinculo.VinculoApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = VinculoApplication.class)
@ActiveProfiles("test")
@Transactional
class DonationPersistenceTest {

    @Autowired
    private DonationRepository donationRepository;

    @Test
    void shouldPersistDonationOffer() {
        var donor = new Donor("John Doe", "john@test.com", DonorType.INDIVIDUAL);
        var donation = new DonationOffer(donor);

        var item = new DonationItem(
            UUID.randomUUID(),
            new BigDecimal("50"),
            QuantityUnit.KG,
            LocalDate.now().plusMonths(6)
        );
        donation.addItem(item);

        var saved = donationRepository.save(donation);
        var found = donationRepository.findById(saved.getId()).orElse(null);

        assertNotNull(found);
        assertEquals(DonationStatus.PENDING, found.getStatus());
        assertEquals("John Doe", found.getDonor().name());
        assertEquals(1, found.getItems().size());
    }

    @Test
    void shouldPersistDonationWithMultipleItems() {
        var donor = new Donor("Company", "company@test.com", DonorType.COMPANY);
        var donation = new DonationOffer(donor);

        donation.addItem(new DonationItem(UUID.randomUUID(), new BigDecimal("100"), QuantityUnit.KG, null));
        donation.addItem(new DonationItem(UUID.randomUUID(), new BigDecimal("50"), QuantityUnit.LITER, LocalDate.now().plusMonths(3)));

        var saved = donationRepository.save(donation);
        var found = donationRepository.findById(saved.getId()).orElse(null);

        assertNotNull(found);
        assertEquals(2, found.getItems().size());
    }

    @Test
    void shouldUpdateDonationStatus() {
        var donor = new Donor("Test", "test@test.com", DonorType.INDIVIDUAL);
        var donation = new DonationOffer(donor);

        var saved = donationRepository.save(donation);
        
        donation.accept(UUID.randomUUID());

        var found = donationRepository.findById(saved.getId()).orElse(null);

        assertEquals(DonationStatus.ACCEPTED, found.getStatus());
    }
}
package com.vinculo.unit.domain.donation.model;

import com.vinculo.domain.donation.model.DonationOffer;
import com.vinculo.domain.donation.model.DonationStatus;
import com.vinculo.domain.donation.model.Donor;
import com.vinculo.domain.donation.model.DonorType;
import com.vinculo.domain.donation.model.QuantityUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DonationOffer Aggregate Root")
class DonationOfferTest {

    private Donor donor;
    private UUID warehouseId;

    @BeforeEach
    void setUp() {
        donor = new Donor("John Doe", "john@example.com", DonorType.INDIVIDUAL);
        warehouseId = UUID.randomUUID();
    }

    @Nested
    @DisplayName("Creation")
    class CreationTests {

        @Test
        @DisplayName("should be created with PENDING status")
        void shouldBeCreatedWithPendingStatus() {
            var donation = DonationOffer.builder()
                .donor(donor)
                .build();

            assertEquals(DonationStatus.PENDING, donation.getStatus());
            assertNotNull(donation.getCreatedAt());
        }

        @Test
        @DisplayName("should throw when donor is missing")
        void shouldThrowWhenDonorMissing() {
            assertThrows(IllegalArgumentException.class, () ->
                DonationOffer.builder().build()
            );
        }
    }

    @Nested
    @DisplayName("Add Items")
    class AddItemsTests {

        @Test
        @DisplayName("should add item to donation")
        void shouldAddItem() {
            var donation = DonationOffer.builder().donor(donor).build();
            var productId = UUID.randomUUID();

            donation.addItem(productId, new BigDecimal("50"), QuantityUnit.KG, LocalDate.now().plusMonths(6));

            assertEquals(1, donation.getItems().size());
            assertEquals(productId, donation.getItems().getFirst().getProductId());
        }
    }

    @Nested
    @DisplayName("Accept Transition")
    class AcceptTransitionTests {

        @Test
        @DisplayName("should accept pending donation")
        void shouldAcceptPendingDonation() {
            var donation = createPendingDonation();

            donation.accept(warehouseId);

            assertEquals(DonationStatus.ACCEPTED, donation.getStatus());
            assertEquals(warehouseId, donation.getWarehouseId());
        }

        @Test
        @DisplayName("should throw when accepting already accepted donation")
        void shouldThrowWhenAcceptingAcceptedDonation() {
            var donation = createPendingDonation();
            donation.accept(warehouseId);

            assertThrows(IllegalStateException.class, () ->
                donation.accept(warehouseId)
            );
        }

        @Test
        @DisplayName("should throw when accepting rejected donation")
        void shouldThrowWhenAcceptingRejectedDonation() {
            var donation = createPendingDonation();
            donation.reject();

            assertThrows(IllegalStateException.class, () ->
                donation.accept(warehouseId)
            );
        }
    }

    @Nested
    @DisplayName("Reject Transition")
    class RejectTransitionTests {

        @Test
        @DisplayName("should reject pending donation")
        void shouldRejectPendingDonation() {
            var donation = createPendingDonation();

            donation.reject();

            assertEquals(DonationStatus.REJECTED, donation.getStatus());
        }

        @Test
        @DisplayName("should throw when rejecting accepted donation")
        void shouldThrowWhenRejectingAcceptedDonation() {
            var donation = createPendingDonation();
            donation.accept(warehouseId);

            assertThrows(IllegalStateException.class, donation::reject);
        }

        @Test
        @DisplayName("should throw when rejecting already rejected donation")
        void shouldThrowWhenRejectingRejectedDonation() {
            var donation = createPendingDonation();
            donation.reject();

            assertThrows(IllegalStateException.class, donation::reject);
        }
    }

    @Nested
    @DisplayName("Redirect Transition")
    class RedirectTransitionTests {

        @Test
        @DisplayName("should redirect pending donation")
        void shouldRedirectPendingDonation() {
            var donation = createPendingDonation();

            donation.redirect();

            assertEquals(DonationStatus.REDIRECTED, donation.getStatus());
        }

        @Test
        @DisplayName("should throw when redirecting accepted donation")
        void shouldThrowWhenRedirectingAcceptedDonation() {
            var donation = createPendingDonation();
            donation.accept(warehouseId);

            assertThrows(IllegalStateException.class, donation::redirect);
        }
    }

    private DonationOffer createPendingDonation() {
        var donation = DonationOffer.builder().donor(donor).build();
        donation.addItem(UUID.randomUUID(), new BigDecimal("50"), QuantityUnit.KG, LocalDate.now().plusMonths(6));
        return donation;
    }
}
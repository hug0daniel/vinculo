package com.vinculo.unit.application.inventory;

import com.vinculo.application.inventory.FefoStockService;
import com.vinculo.domain.inventory.model.Lot;
import com.vinculo.domain.inventory.model.Unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("FefoStockService")
class FefoStockServiceTest {

    private static final LocalDate EARLY_EXPIRY = LocalDate.of(2026, 6, 1);
    private static final LocalDate LATE_EXPIRY = LocalDate.of(2026, 9, 1);

    private FefoStockService fefoStockService;
    private UUID productId;

    @BeforeEach
    void setUp() {
        fefoStockService = new FefoStockService();
        productId = UUID.randomUUID();
    }

    @Nested
    @DisplayName("Allocate Stock")
    class AllocateStockTests {

        @Test
        @DisplayName("should allocate from earliest expiry first")
        void shouldAllocateFEFO() {
            Lot lot1 = new Lot(productId, new BigDecimal("30"), Unit.KG, EARLY_EXPIRY);
            Lot lot2 = new Lot(productId, new BigDecimal("50"), Unit.KG, LATE_EXPIRY);
            List<Lot> lots = List.of(lot1, lot2);

            List<FefoStockService.LotAllocation> allocations = fefoStockService.allocateStock(
                    productId,
                    new BigDecimal("60"),
                    lots
            );

            assertEquals(2, allocations.size());
            assertEquals(new BigDecimal("30"), allocations.getFirst().quantity());
            assertEquals(new BigDecimal("30"), allocations.get(1).quantity());
            assertEquals(EARLY_EXPIRY, allocations.getFirst().lot().getExpiryDate());
            assertEquals(LATE_EXPIRY, allocations.get(1).lot().getExpiryDate());
        }

        @Test
        @DisplayName("should allocate from single lot when sufficient")
        void shouldAllocateSingleLot() {
            Lot lot = new Lot(productId, new BigDecimal("100"), Unit.KG, LATE_EXPIRY);

            List<FefoStockService.LotAllocation> allocations = fefoStockService.allocateStock(
                    productId,
                    new BigDecimal("25"),
                    List.of(lot)
            );

            assertEquals(1, allocations.size());
            assertEquals(new BigDecimal("25"), allocations.getFirst().quantity());
            assertEquals(LATE_EXPIRY, allocations.getFirst().lot().getExpiryDate());
        }

        @Test
        @DisplayName("should partially allocate when stock is insufficient")
        void shouldPartiallyAllocateWhenInsufficientStock() {
            Lot lot = new Lot(productId, new BigDecimal("10"), Unit.KG, LATE_EXPIRY);

            List<FefoStockService.LotAllocation> allocations = fefoStockService.allocateStock(
                    productId,
                    new BigDecimal("50"),
                    List.of(lot)
            );

            assertEquals(1, allocations.size());
            assertEquals(new BigDecimal("10"), allocations.getFirst().quantity());
            assertEquals(LATE_EXPIRY, allocations.getFirst().lot().getExpiryDate());
        }
    }

    @Nested
    @DisplayName("Total Available")
    class TotalAvailableTests {

        @Test
        @DisplayName("should calculate total available")
        void shouldCalculateTotalAvailable() {
            Lot lot1 = new Lot(productId, new BigDecimal("30"), Unit.KG, EARLY_EXPIRY);
            Lot lot2 = new Lot(productId, new BigDecimal("50"), Unit.KG, LATE_EXPIRY);

            BigDecimal total = fefoStockService.getTotalAvailableStock(List.of(lot1, lot2));

            assertEquals(new BigDecimal("80"), total);
        }

        @Test
        @DisplayName("should return zero when no stock")
        void shouldReturnZeroWhenNoStock() {
            BigDecimal total = fefoStockService.getTotalAvailableStock(List.of());

            assertEquals(BigDecimal.ZERO, total);
        }
    }
}
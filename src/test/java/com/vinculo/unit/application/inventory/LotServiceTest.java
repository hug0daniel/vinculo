package com.vinculo.unit.application.inventory;

import com.vinculo.api.warehouse.dto.LotRequest;
import com.vinculo.application.exception.ResourceNotFoundException;
import com.vinculo.application.inventory.LotService;
import com.vinculo.domain.inventory.model.Lot;
import com.vinculo.domain.inventory.model.Unit;
import com.vinculo.domain.inventory.model.Warehouse;
import com.vinculo.domain.inventory.repository.LotRepository;
import com.vinculo.domain.inventory.repository.WarehouseRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LotService")
class LotServiceTest {

    @Mock
    private LotRepository lotRepository;

    @Mock
    private WarehouseRepository warehouseRepository;

    private LotService lotService;

    private UUID warehouseId;
    private UUID lotId;

    @BeforeEach
    void setUp() {
        lotService = new com.vinculo.application.inventory.LotServiceImpl(lotRepository, warehouseRepository);
        warehouseId = UUID.randomUUID();
        lotId = UUID.randomUUID();
    }

    @Nested
    @DisplayName("Create Lot")
    class CreateLotTests {

        @Test
        @DisplayName("should create lot")
        void shouldCreateLot() {
            var warehouse = Warehouse.builder()
                    .name("Test")
                    .type(com.vinculo.domain.inventory.model.WarehouseType.WAREHOUSE)
                    .build();

var request = new LotRequest(
                    "Rice",
                    new BigDecimal("50"),
                    Unit.KG,
                    LocalDate.now().plusMonths(6),
                    "LOT-001"
            );

            when(warehouseRepository.findById(warehouseId)).thenReturn(Optional.of(warehouse));
            when(lotRepository.save(any(Lot.class))).thenAnswer(inv -> inv.getArgument(0));

            var result = lotService.createLot(warehouseId, request);

            assertNotNull(result);
            assertEquals("50", result.quantity().toString());
            assertEquals(Unit.KG, result.unit());
        }

        @Test
        @DisplayName("should throw when warehouse not found")
        void shouldThrowWhenWarehouseNotFound() {
            var request = new LotRequest(
                    "Rice",
                    new BigDecimal("50"),
                    Unit.KG,
                    LocalDate.now().plusMonths(6),
                    null
            );

            when(warehouseRepository.findById(warehouseId)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () ->
                    lotService.createLot(warehouseId, request)
            );
        }
    }

    @Nested
    @DisplayName("Get Lot")
    class GetLotTests {

        @Test
        @DisplayName("should get lot by id")
        void shouldGetLotById() {
            var lot = new Lot("Rice", new BigDecimal("50"), Unit.KG, LocalDate.now().plusMonths(6));

            when(lotRepository.findById(lotId)).thenReturn(Optional.of(lot));

            var result = lotService.getLot(lotId);

            assertNotNull(result);
        }

        @Test
        @DisplayName("should throw when lot not found")
        void shouldThrowWhenLotNotFound() {
            when(lotRepository.findById(lotId)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () ->
                    lotService.getLot(lotId)
            );
        }

        @Test
        @DisplayName("should get lots by warehouse")
        void shouldGetLotsByWarehouse() {
            var lot = new Lot("Rice", new BigDecimal("50"), Unit.KG, LocalDate.now().plusMonths(6));

            when(lotRepository.findByWarehouseId(warehouseId)).thenReturn(List.of(lot));

            var result = lotService.getLotsByWarehouse(warehouseId);

            assertEquals(1, result.size());
        }
    }
}
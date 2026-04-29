package com.vinculo.unit.application.inventory;

import com.vinculo.api.warehouse.dto.WarehouseRequest;
import com.vinculo.api.warehouse.dto.WarehouseResponse;
import com.vinculo.application.exception.ResourceNotFoundException;
import com.vinculo.application.inventory.WarehouseService;
import com.vinculo.domain.inventory.model.WarehouseType;
import com.vinculo.domain.inventory.model.WarehouseStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("WarehouseService")
class WarehouseServiceTest {

    @Mock
    private com.vinculo.domain.inventory.repository.WarehouseRepository warehouseRepository;

    @Mock
    private com.vinculo.domain.inventory.repository.LotRepository lotRepository;

    private WarehouseService warehouseService;

    private UUID warehouseId;

    @BeforeEach
    void setUp() {
        warehouseService = new com.vinculo.application.inventory.WarehouseServiceImpl(warehouseRepository, lotRepository);
        warehouseId = UUID.randomUUID();
    }

    @Nested
    @DisplayName("Create Warehouse")
    class CreateWarehouseTests {

        @Test
        @DisplayName("should create warehouse with all fields")
        void shouldCreateWarehouse() {
            var request = new WarehouseRequest(
                    "Warehouse Porto",
                    WarehouseType.WAREHOUSE,
                    "Rua Porto",
                    41.1579,
                    -8.6291
            );

            when(warehouseRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

            var result = warehouseService.createWarehouse(request);

            assertNotNull(result);
            assertEquals("Warehouse Porto", result.name());
            assertEquals(WarehouseType.WAREHOUSE, result.type());
            assertEquals(WarehouseStatus.ACTIVE, result.status());
        }

        @Test
        @DisplayName("should create warehouse without location")
        void shouldCreateWarehouseWithoutLocation() {
            var request = new WarehouseRequest(
                    "Warehouse Lisboa",
                    WarehouseType.WAREHOUSE,
                    null,
                    null,
                    null
            );

            when(warehouseRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

            var result = warehouseService.createWarehouse(request);

            assertNotNull(result);
            assertNull(result.address());
        }
    }

    @Nested
    @DisplayName("Get Warehouse")
    class GetWarehouseTests {

        @Test
        @DisplayName("should get warehouse by id")
        void shouldGetWarehouseById() {
            var warehouse = com.vinculo.domain.inventory.model.Warehouse.builder()
                    .name("Test")
                    .type(WarehouseType.WAREHOUSE)
                    .build();

            when(warehouseRepository.findById(warehouseId)).thenReturn(Optional.of(warehouse));

            var result = warehouseService.getWarehouse(warehouseId);

            assertNotNull(result);
            assertEquals("Test", result.name());
        }

        @Test
        @DisplayName("should throw when warehouse not found")
        void shouldThrowWhenNotFound() {
            when(warehouseRepository.findById(warehouseId)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class, () ->
                    warehouseService.getWarehouse(warehouseId)
            );
        }

        @Test
        @DisplayName("should get all warehouses")
        void shouldGetAllWarehouses() {
            var warehouse = com.vinculo.domain.inventory.model.Warehouse.builder()
                    .name("Test")
                    .type(WarehouseType.WAREHOUSE)
                    .build();

            when(warehouseRepository.findAll()).thenReturn(List.of(warehouse));

            var result = warehouseService.getAllWarehouses();

            assertEquals(1, result.size());
        }
    }

    @Nested
    @DisplayName("Activate/Deactivate")
    class ActivateDeactivateTests {

        @Test
        @DisplayName("should deactivate active warehouse")
        void shouldDeactivateWarehouse() {
            var warehouse = com.vinculo.domain.inventory.model.Warehouse.builder()
                    .name("Test")
                    .type(WarehouseType.WAREHOUSE)
                    .build();

            when(warehouseRepository.findById(warehouseId)).thenReturn(Optional.of(warehouse));

            var result = warehouseService.deactivateWarehouse(warehouseId);

            assertEquals(WarehouseStatus.INACTIVE, result.status());
        }

        @Test
        @DisplayName("should throw when deactivating inactive warehouse")
        void shouldThrowWhenDeactivatingInactive() {
            var warehouse = com.vinculo.domain.inventory.model.Warehouse.builder()
                    .name("Test")
                    .type(WarehouseType.WAREHOUSE)
                    .build();
            warehouse.deactivate();

            when(warehouseRepository.findById(warehouseId)).thenReturn(Optional.of(warehouse));

            assertThrows(IllegalStateException.class, () ->
                    warehouseService.deactivateWarehouse(warehouseId)
            );
        }
    }
}
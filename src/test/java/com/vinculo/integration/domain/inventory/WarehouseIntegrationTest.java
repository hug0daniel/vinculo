package com.vinculo.integration.domain.inventory;

import com.vinculo.VinculoApplication;
import com.vinculo.domain.inventory.model.Lot;
import com.vinculo.domain.inventory.model.Unit;
import com.vinculo.domain.inventory.model.Warehouse;
import com.vinculo.domain.inventory.model.WarehouseType;
import com.vinculo.domain.inventory.repository.LotRepository;
import com.vinculo.domain.inventory.repository.WarehouseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = VinculoApplication.class)
@ActiveProfiles("test")
@DisplayName("Warehouse Integration Tests")
class WarehouseIntegrationTest {

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private LotRepository lotRepository;

    @BeforeEach
    void setUp() {
        lotRepository.deleteAll();
        warehouseRepository.deleteAll();
    }

    @Test
    @DisplayName("should create and persist warehouse")
    void shouldPersistWarehouse() {
        var warehouse = Warehouse.builder()
                .name("Test Warehouse")
                .type(WarehouseType.WAREHOUSE)
                .build();

        var saved = warehouseRepository.save(warehouse);

        assertNotNull(saved.getId());
        assertEquals("Test Warehouse", saved.getName());
    }

    @Test
    @DisplayName("should add lot to warehouse")
    void shouldAddLotToWarehouse() {
        var warehouse = warehouseRepository.save(Warehouse.builder()
                .name("Test")
                .type(WarehouseType.WAREHOUSE)
                .build());

        var lot = new Lot("Rice", new BigDecimal("50"), Unit.KG, LocalDate.now().plusMonths(6));
        warehouse.addLot(lot);
        lotRepository.save(lot);

        var lots = lotRepository.findByWarehouseId(warehouse.getId());

        assertEquals(1, lots.size());
    }

    @Test
    @DisplayName("should query lots by FEFO")
    void shouldQueryLotsByFefo() {
        var warehouse = warehouseRepository.save(Warehouse.builder()
                .name("Test")
                .type(WarehouseType.WAREHOUSE)
                .build());

        var lot1 = new Lot("Rice", new BigDecimal("30"), Unit.KG, LocalDate.now().plusMonths(3));
        var lot2 = new Lot("Beans", new BigDecimal("50"), Unit.KG, LocalDate.now().plusMonths(6));

        warehouse.addLot(lot1);
        warehouse.addLot(lot2);
        lotRepository.save(lot1);
        lotRepository.save(lot2);

        var lots = lotRepository.findByWarehouse_IdOrderByExpiryDateAsc(warehouse.getId());

        assertEquals(2, lots.size());
    }
}
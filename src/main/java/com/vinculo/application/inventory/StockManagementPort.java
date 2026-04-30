package com.vinculo.application.inventory;

import com.vinculo.api.warehouse.dto.LotRequest;
import com.vinculo.api.warehouse.dto.LotResponse;

import java.util.UUID;

public interface StockManagementPort {

    LotResponse createLotInWarehouse(UUID warehouseId, LotRequest request);

    void allocateStock(UUID warehouseId, String productName, int quantity);
}
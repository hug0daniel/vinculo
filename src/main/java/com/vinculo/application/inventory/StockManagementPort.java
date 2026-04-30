package com.vinculo.application.inventory;

import com.vinculo.api.lot.controller.dto.LotRequest;
import com.vinculo.api.lot.controller.dto.LotResponse;

import java.util.UUID;

public interface StockManagementPort {

    LotResponse createLotInWarehouse(UUID warehouseId, LotRequest request);

    void allocateStock(UUID warehouseId, String productName, int quantity);
}
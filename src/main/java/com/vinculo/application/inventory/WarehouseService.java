package com.vinculo.application.inventory;

import com.vinculo.api.warehouse.dto.WarehouseRequest;
import com.vinculo.api.warehouse.dto.WarehouseResponse;
import com.vinculo.api.warehouse.dto.StockResponse;

import java.util.List;
import java.util.UUID;

public interface WarehouseService {

    WarehouseResponse createWarehouse(WarehouseRequest request);

    WarehouseResponse getWarehouse(UUID id);

    List<WarehouseResponse> getAllWarehouses();

    StockResponse getStock(UUID warehouseId);

    WarehouseResponse deactivateWarehouse(UUID id);

    WarehouseResponse activateWarehouse(UUID id);
}
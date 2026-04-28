package com.vinculo.application.inventory;

import com.vinculo.api.warehouse.dto.WarehouseRequest;
import com.vinculo.api.warehouse.dto.WarehouseResponse;

import java.util.List;
import java.util.UUID;

public interface WarehouseService {

    WarehouseResponse createWarehouse(WarehouseRequest request);

    WarehouseResponse getWarehouse(UUID id);

    List<WarehouseResponse> getAllWarehouses();

    WarehouseResponse deactivateWarehouse(UUID id);

    WarehouseResponse activateWarehouse(UUID id);
}
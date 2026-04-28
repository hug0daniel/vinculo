package com.vinculo.application.inventory;

import com.vinculo.api.warehouse.dto.WarehouseRequest;
import com.vinculo.api.warehouse.dto.WarehouseResponse;
import com.vinculo.api.warehouse.utils.WarehouseMapper;
import com.vinculo.application.exception.ResourceNotFoundException;
import com.vinculo.domain.inventory.model.Warehouse;
import com.vinculo.domain.inventory.model.WarehouseStatus;
import com.vinculo.domain.inventory.repository.WarehouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;

    public WarehouseServiceImpl(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    @Transactional
    public WarehouseResponse createWarehouse(WarehouseRequest request) {
        Warehouse warehouse = WarehouseMapper.toEntity(request);
        return WarehouseMapper.toResponse(warehouseRepository.save(warehouse));
    }

    @Override
    public WarehouseResponse getWarehouse(UUID id) {
        return WarehouseMapper.toResponse(findWarehouseById(id));
    }

    @Override
    public List<WarehouseResponse> getAllWarehouses() {
        return warehouseRepository.findAll().stream()
                .map(WarehouseMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public WarehouseResponse deactivateWarehouse(UUID id) {
        Warehouse warehouse = findWarehouseById(id);

        if (warehouse.getStatus() == WarehouseStatus.INACTIVE) {
            throw new IllegalStateException("Warehouse is already inactive: " + id);
        }

        warehouse.deactivate();
        return WarehouseMapper.toResponse(warehouse);
    }

    @Override
    @Transactional
    public WarehouseResponse activateWarehouse(UUID id) {
        Warehouse warehouse = findWarehouseById(id);

        if (warehouse.getStatus() == WarehouseStatus.ACTIVE) {
            throw new IllegalStateException("Warehouse is already active: " + id);
        }

        warehouse.activate();
        return WarehouseMapper.toResponse(warehouse);
    }

    private Warehouse findWarehouseById(UUID id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found: " + id));
    }
}
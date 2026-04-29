package com.vinculo.application.inventory;

import com.vinculo.api.warehouse.dto.WarehouseRequest;
import com.vinculo.api.warehouse.dto.WarehouseResponse;
import com.vinculo.api.warehouse.dto.StockResponse;
import com.vinculo.api.warehouse.utils.WarehouseMapper;
import com.vinculo.application.exception.ResourceNotFoundException;
import com.vinculo.domain.inventory.model.Lot;
import com.vinculo.domain.inventory.model.Warehouse;
import com.vinculo.domain.inventory.model.WarehouseStatus;
import com.vinculo.domain.inventory.repository.LotRepository;
import com.vinculo.domain.inventory.repository.WarehouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final LotRepository lotRepository;

    public WarehouseServiceImpl(WarehouseRepository warehouseRepository, LotRepository lotRepository) {
        this.warehouseRepository = warehouseRepository;
        this.lotRepository = lotRepository;
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
    public StockResponse getStock(UUID warehouseId) {
        Warehouse warehouse = findWarehouseById(warehouseId);

        List<Lot> lots = lotRepository.findByWarehouseId(warehouseId);
        BigDecimal total = lots.stream()
                .filter(Lot::hasStock)
                .map(Lot::getQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<StockResponse.StockItem> items = lots.stream()
                .filter(Lot::hasStock)
                .map(lot -> new StockResponse.StockItem(
                        lot.getId(),
                        lot.getProductName(),
                        lot.getQuantity(),
                        lot.getUnit(),
                        lot.getExpiryDate()
                ))
                .toList();

        return new StockResponse(warehouseId, warehouse.getName(), items, total);
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
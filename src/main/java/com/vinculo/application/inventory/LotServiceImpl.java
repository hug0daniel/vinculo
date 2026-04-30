package com.vinculo.application.inventory;

import com.vinculo.api.lot.controller.dto.LotRequest;
import com.vinculo.api.lot.controller.dto.LotResponse;
import com.vinculo.api.warehouse.utils.LotMapper;
import com.vinculo.application.exception.ResourceNotFoundException;
import com.vinculo.application.inventory.FefoStockService;
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
@Transactional
public class LotServiceImpl implements LotService, StockManagementPort {

    private final LotRepository lotRepository;
    private final WarehouseRepository warehouseRepository;
    private final FefoStockService fefoStockService;

    public LotServiceImpl(LotRepository lotRepository,
                       WarehouseRepository warehouseRepository,
                       FefoStockService fefoStockService) {
        this.lotRepository = lotRepository;
        this.warehouseRepository = warehouseRepository;
        this.fefoStockService = fefoStockService;
    }

    @Override
    public LotResponse createLot(UUID warehouseId, LotRequest request) {
        return createLotInWarehouse(warehouseId, request);
    }

    @Override
    public LotResponse createLotInWarehouse(UUID warehouseId, LotRequest request) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found: " + warehouseId));

        if (warehouse.getStatus() != WarehouseStatus.ACTIVE) {
            throw new IllegalStateException("Warehouse is not active");
        }

        Lot lot = LotMapper.toEntity(request, warehouse);
        return LotMapper.toResponse(lotRepository.save(lot));
    }

    @Override
    public LotResponse getLot(UUID id) {
        Lot lot = lotRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lot not found: " + id));
        return LotMapper.toResponse(lot);
    }

    @Override
    public List<LotResponse> getLotsByWarehouse(UUID warehouseId) {
        return lotRepository.findByWarehouseId(warehouseId).stream()
                .map(LotMapper::toResponse)
                .toList();
    }

    @Override
    public void allocateStock(UUID warehouseId, String productName, int quantity) {
        List<Lot> availableLots = lotRepository
                .findByWarehouseIdAndProductNameAndExpiryDateIsNotNullOrderByExpiryDateAsc(warehouseId, productName);

        BigDecimal quantityToAllocate = BigDecimal.valueOf(quantity);
        List<FefoStockService.LotAllocation> allocations = fefoStockService.allocateStock(
                productName, quantityToAllocate, availableLots);

        for (FefoStockService.LotAllocation allocation : allocations) {
            Lot lot = allocation.lot();
            BigDecimal allocatedQty = allocation.quantity();
            lot.setQuantity(lot.getQuantity().subtract(allocatedQty));
        }
    }
}
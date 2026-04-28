package com.vinculo.application.inventory;

import com.vinculo.api.warehouse.dto.LotRequest;
import com.vinculo.api.warehouse.dto.LotResponse;
import com.vinculo.api.warehouse.utils.LotMapper;
import com.vinculo.application.exception.ResourceNotFoundException;
import com.vinculo.domain.inventory.model.Lot;
import com.vinculo.domain.inventory.model.Warehouse;
import com.vinculo.domain.inventory.repository.LotRepository;
import com.vinculo.domain.inventory.repository.WarehouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class LotServiceImpl implements LotService {

    private final LotRepository lotRepository;
    private final WarehouseRepository warehouseRepository;

    public LotServiceImpl(LotRepository lotRepository, WarehouseRepository warehouseRepository) {
        this.lotRepository = lotRepository;
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public LotResponse createLot(UUID warehouseId, LotRequest request) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found: " + warehouseId));

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
}
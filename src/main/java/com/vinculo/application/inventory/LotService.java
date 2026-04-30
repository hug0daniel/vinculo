package com.vinculo.application.inventory;

import com.vinculo.api.lot.controller.dto.LotRequest;
import com.vinculo.api.lot.controller.dto.LotResponse;
import java.util.List;
import java.util.UUID;

public interface LotService {

    LotResponse createLot(UUID warehouseId, LotRequest request);

    LotResponse getLot(UUID id);

    List<LotResponse> getLotsByWarehouse(UUID warehouseId);
}
package com.vinculo.api.warehouse.utils;

import com.vinculo.api.warehouse.dto.LotRequest;
import com.vinculo.api.warehouse.dto.LotResponse;
import com.vinculo.domain.inventory.model.Lot;
import com.vinculo.domain.inventory.model.Warehouse;

public class LotMapper {

    public static LotResponse toResponse(Lot lot) {
        return new LotResponse(
                lot.getId(),
                lot.getProductName(),
                lot.getLotNumber(),
                lot.getQuantity(),
                lot.getInitialQuantity(),
                lot.getUnit(),
                lot.getExpiryDate(),
                lot.getReceivedAt(),
                lot.getWarehouse() != null ? lot.getWarehouse().getId() : null
        );
    }

    public static Lot toEntity(LotRequest request, Warehouse warehouse) {
        Lot lot = new Lot(
                request.productName(),
                request.quantity(),
                request.unit(),
                request.expiryDate()
        );

        if (request.lotNumber() != null) {
            lot.setLotNumber(request.lotNumber());
        }

        warehouse.addLot(lot);
        return lot;
    }
}
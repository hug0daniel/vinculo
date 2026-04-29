package com.vinculo.api.warehouse.dto;

import com.vinculo.domain.inventory.model.Unit;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record StockResponse(
    UUID warehouseId,
    String warehouseName,
    List<StockItem> items,
    BigDecimal totalQuantity
) {
    public record StockItem(
        UUID lotId,
        String productName,
        BigDecimal quantity,
        Unit unit,
        LocalDate expiryDate
    ) {}
}
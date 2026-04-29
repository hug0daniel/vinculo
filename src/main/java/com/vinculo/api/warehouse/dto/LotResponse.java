package com.vinculo.api.warehouse.dto;

import com.vinculo.domain.inventory.model.Unit;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record LotResponse(
    UUID id,
    String productName,
    String lotNumber,
    BigDecimal quantity,
    BigDecimal initialQuantity,
    Unit unit,
    LocalDate expiryDate,
    LocalDateTime receivedAt,
    UUID warehouseId
) {}
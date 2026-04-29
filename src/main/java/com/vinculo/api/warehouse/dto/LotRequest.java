package com.vinculo.api.warehouse.dto;

import com.vinculo.domain.inventory.model.Unit;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public record LotRequest(
    @NotNull
    String productName,

    @NotNull @Positive
    BigDecimal quantity,

    @NotNull
    Unit unit,

    LocalDate expiryDate,

    String lotNumber
) {}
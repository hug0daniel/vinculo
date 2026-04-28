package com.vinculo.api.warehouse.dto;

import com.vinculo.domain.inventory.model.WarehouseType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WarehouseRequest(
    @NotBlank
    String name,

    @NotNull
    WarehouseType type,

    String address,

    Double latitude,

    Double longitude
) {}
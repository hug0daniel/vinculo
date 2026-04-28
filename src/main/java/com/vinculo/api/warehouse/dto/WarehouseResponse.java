package com.vinculo.api.warehouse.dto;

import com.vinculo.domain.inventory.model.WarehouseStatus;
import com.vinculo.domain.inventory.model.WarehouseType;

import java.time.LocalDateTime;
import java.util.UUID;

public record WarehouseResponse(
    UUID id,
    String name,
    WarehouseType type,
    WarehouseStatus status,
    String address,
    Double latitude,
    Double longitude,
    LocalDateTime createdAt
) {}
package com.vinculo.api.warehouse.utils;

import com.vinculo.api.warehouse.dto.WarehouseRequest;
import com.vinculo.api.warehouse.dto.WarehouseResponse;
import com.vinculo.domain.inventory.model.Location;
import com.vinculo.domain.inventory.model.Warehouse;

public class WarehouseMapper {

    public static WarehouseResponse toResponse(Warehouse warehouse) {
        Location location = warehouse.getLocation();
        return new WarehouseResponse(
                warehouse.getId(),
                warehouse.getName(),
                warehouse.getType(),
                warehouse.getStatus(),
                location != null ? location.getAddress() : null,
                location != null ? location.getLatitude() : null,
                location != null ? location.getLongitude() : null,
                warehouse.getCreatedAt()
        );
    }

    public static Warehouse toEntity(WarehouseRequest request) {
        Location location = null;
        if (request.address() != null || request.latitude() != null || request.longitude() != null) {
            location = new Location(request.address(), request.latitude(), request.longitude());
        }

        return Warehouse.builder()
                .name(request.name())
                .type(request.type())
                .location(location)
                .build();
    }
}
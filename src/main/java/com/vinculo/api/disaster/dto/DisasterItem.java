package com.vinculo.api.disaster.dto;

import com.vinculo.domain.disaster.model.DisasterStatus;
import com.vinculo.domain.disaster.model.DisasterType;

import java.util.UUID;

public record DisasterItem(
        UUID id,
        String name,
        DisasterType type,
        DisasterStatus status,
        String location
) {}

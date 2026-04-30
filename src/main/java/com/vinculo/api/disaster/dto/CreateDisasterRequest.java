package com.vinculo.api.disaster.dto;

import com.vinculo.domain.disaster.model.DisasterType;

public record CreateDisasterRequest(
        String name,
        DisasterType type,
        String location
) {}

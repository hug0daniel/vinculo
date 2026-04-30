package com.vinculo.api.disaster.dto;

import com.vinculo.domain.disaster.model.DisasterType;

public record UpdateDisasterRequest(
        String name,
        DisasterType type,
        String location
) {}
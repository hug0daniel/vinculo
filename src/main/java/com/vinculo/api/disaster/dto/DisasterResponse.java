package com.vinculo.api.disaster.dto;

import com.vinculo.domain.disaster.model.DisasterStatus;
import com.vinculo.domain.disaster.model.DisasterType;

import java.time.LocalDate;
import java.util.UUID;

public record DisasterResponse(
        UUID id,
        String name,
        DisasterType type,
        DisasterStatus status,
        String location,
        LocalDate startDate,
        LocalDate endDate
) {}

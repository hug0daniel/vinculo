package com.vinculo.api.disaster.dto;

import com.vinculo.domain.disaster.model.DisasterStatus;
import com.vinculo.domain.disaster.model.DisasterType;

import java.time.LocalDate;
import java.util.UUID;

public class DisasterDto {

    public record DisasterResponse(
        UUID id,
        String name,
        DisasterType type,
        DisasterStatus status,
        String location,
        LocalDate startDate,
        LocalDate endDate
    ) {}

    public record CreateDisasterRequest(
        String name,
        DisasterType type,
        String location
    ) {}

    public record UpdateDisasterRequest(
        String name,
        DisasterType type,
        String location
    ) {}

    public record DisasterListItem(
        UUID id,
        String name,
        DisasterType type,
        DisasterStatus status,
        String location
    ) {}
}

package com.vinculo.api.request.dto;

import com.vinculo.domain.request.model.RequestStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record RequestListItem(
        UUID id,
        LocalDateTime createdAt,
        RequestStatus status,
        UUID disasterId,
        String beneficiaryName,
        int itemsCount
) {}

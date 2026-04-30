package com.vinculo.api.request.dto;

import com.vinculo.api.request.dto.BeneficiaryDto;
import com.vinculo.domain.request.model.RequestStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record RequestResponse(
        UUID id,
        LocalDateTime createdAt,
        RequestStatus status,
        UUID disasterId,
        BeneficiaryDto beneficiary,
        List<ItemDto> items
) {}

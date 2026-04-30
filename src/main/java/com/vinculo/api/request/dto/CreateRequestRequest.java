package com.vinculo.api.request.dto;

import com.vinculo.api.request.dto.BeneficiaryDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateRequestRequest(
        @NotNull(message = "Beneficiary is required")
        @Valid
        BeneficiaryDto beneficiary,
        @NotNull(message = "Disaster ID is required")
        UUID disasterId,
        @NotNull(message = "At least one item is required")
        @Valid
        List<RequestItemDto> items
) {}

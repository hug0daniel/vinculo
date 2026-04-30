package com.vinculo.api.request.dto;

import java.math.BigDecimal;
import com.vinculo.domain.request.model.QuantityUnit;

public record RequestItemDto(
        String productName,
        BigDecimal quantity,
        QuantityUnit unit
) {}

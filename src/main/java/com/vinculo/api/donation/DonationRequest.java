package com.vinculo.api.donation;

import com.vinculo.domain.donation.model.DonorType;
import com.vinculo.domain.donation.model.QuantityUnit;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record DonationRequest(
    @NotNull(message = "Donor is required")
    @Valid
    DonorDto donor,
    @NotEmpty(message = "At least one item is required")
    @Valid
    List<DonationItemDto> items
) {
    public record DonorDto(
        String name, // if name null donor = anonymous
        String contact,
        @NotNull(message = "Type is required")
        DonorType type
    ) {}

    public record DonationItemDto(
        @NotNull(message = "Product ID is required")
        UUID productId,
        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be positive")
        BigDecimal quantity,
        @NotNull(message = "Unit is required")
        QuantityUnit unit,
        LocalDate expiryDate
    ) {}
}
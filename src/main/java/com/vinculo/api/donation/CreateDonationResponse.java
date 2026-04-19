package com.vinculo.api.donation;

import com.vinculo.domain.donation.model.DonationStatus;
import com.vinculo.domain.donation.model.QuantityUnit;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record CreateDonationResponse(
    UUID donationId,
    DonationStatus status,
    LocalDateTime createdAt,
    DonorDto donor,
    List<ItemDto> items
) {
    public record DonorDto(String name, String contact, String type) {}
    public record ItemDto(UUID productId, BigDecimal quantity, QuantityUnit unit, LocalDate expiryDate) {}
}
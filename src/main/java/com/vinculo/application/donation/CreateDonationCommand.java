package com.vinculo.application.donation;

import com.vinculo.domain.donation.model.Donor;
import com.vinculo.domain.donation.model.QuantityUnit;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CreateDonationCommand(
    Donor donor,
    List<Item> items
) {
    public record Item(UUID productId, BigDecimal quantity, QuantityUnit unit, LocalDate expiryDate) {}
}
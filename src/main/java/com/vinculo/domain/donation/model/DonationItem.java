package com.vinculo.domain.donation.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
public class DonationItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    private UUID productId;

    @NotNull
    @Positive
    private BigDecimal quantity;

    @NotNull
    @Enumerated(EnumType.STRING)
    private QuantityUnit unit;

    private LocalDate expiryDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donation_id")
    private DonationOffer donation;

    protected DonationItem() {
    }

    public DonationItem(UUID productId, BigDecimal quantity, QuantityUnit unit, LocalDate expiryDate) {
        this.productId = productId;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }

    public UUID getId() {
        return id;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public QuantityUnit getUnit() {
        return unit;
    }

    public void setUnit(QuantityUnit unit) {
        this.unit = unit;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public DonationOffer getDonation() {
        return donation;
    }

    public void setDonation(DonationOffer donation) {
        this.donation = donation;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DonationItem that = (DonationItem) o;
        return Objects.equals(id, that.id) && Objects.equals(productId, that.productId) && Objects.equals(quantity, that.quantity) && unit == that.unit && Objects.equals(expiryDate, that.expiryDate) && Objects.equals(donation, that.donation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productId, quantity, unit, expiryDate, donation);
    }
}
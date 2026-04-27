package com.vinculo.domain.donation.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
public class DonationItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID productId;
    private BigDecimal quantity;

    @Enumerated(EnumType.STRING)
    private QuantityUnit unit;

    private LocalDate expiryDate;

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

    public UUID getId() { return id; }
    public UUID getProductId() { return productId; }
    public BigDecimal getQuantity() { return quantity; }
    public QuantityUnit getUnit() { return unit; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public DonationOffer getDonation() { return donation; }

    void setDonation(DonationOffer donation) {
        this.donation = donation;
    }
}
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

    private String productName;
    private BigDecimal quantity;

    @Enumerated(EnumType.STRING)
    private QuantityUnit unit;

    private LocalDate expiryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donation_id")
    private DonationOffer donation;

    protected DonationItem() {
    }

    public DonationItem(String productName, BigDecimal quantity, QuantityUnit unit, LocalDate expiryDate) {
        this.productName = productName;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }

    public UUID getId() { return id; }
    public String getProductName() { return productName; }
    public BigDecimal getQuantity() { return quantity; }
    public QuantityUnit getUnit() { return unit; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public DonationOffer getDonation() { return donation; }

    void setDonation(DonationOffer donation) {
        this.donation = donation;
    }
}
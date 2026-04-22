package com.vinculo.domain.donation.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Entity
public class DonationItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuantityUnit unit;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "donation_id", nullable = false)
    private DonationOffer donation;

    protected DonationItem() {
    }

    public DonationItem(UUID productId, BigDecimal quantity, QuantityUnit unit, LocalDate expiryDate) {
        this.productId = productId;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }

}
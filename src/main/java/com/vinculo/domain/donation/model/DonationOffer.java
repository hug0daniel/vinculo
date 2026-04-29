package com.vinculo.domain.donation.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class DonationOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private DonationStatus status;

    @Column(name = "warehouse_id")
    private UUID warehouseId;

    @Embedded
    private Donor donor;

    @OneToMany(mappedBy = "donation", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<DonationItem> items = new ArrayList<>();

    protected DonationOffer() {
    }

    private DonationOffer(Builder builder) {
        this.donor = builder.donor;
        this.createdAt = LocalDateTime.now();
        this.status = DonationStatus.PENDING;
    }

    public static Builder builder() {
        return new Builder();
    }

    public UUID getId() { return id; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public DonationStatus getStatus() { return status; }
    public UUID getWarehouseId() { return warehouseId; }
    public Donor getDonor() { return donor; }
    public List<DonationItem> getItems() { return items; }

    public void addItem(String productName, BigDecimal quantity, QuantityUnit unit, LocalDate expiryDate) {
        DonationItem item = new DonationItem(productName, quantity, unit, expiryDate);
        items.add(item);
        item.setDonation(this);
    }

    public void accept(UUID warehouseId) {
        if (this.status != DonationStatus.PENDING) {
            throw new IllegalStateException("Can only accept pending donations");
        }
        this.status = DonationStatus.ACCEPTED;
        this.warehouseId = warehouseId;
    }

    public void reject() {
        if (this.status != DonationStatus.PENDING) {
            throw new IllegalStateException("Can only reject pending donations");
        }
        this.status = DonationStatus.REJECTED;
    }

    public void redirect() {
        if (this.status != DonationStatus.PENDING) {
            throw new IllegalStateException("Can only redirect pending donations");
        }
        this.status = DonationStatus.REDIRECTED;
    }

    public static class Builder {
        private Donor donor;

        public Builder donor(Donor donor) {
            this.donor = donor;
            return this;
        }

        public DonationOffer build() {
            if (donor == null) {
                throw new IllegalArgumentException("Donor is required");
            }
            return new DonationOffer(this);
        }
    }
}
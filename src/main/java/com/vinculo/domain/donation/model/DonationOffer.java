package com.vinculo.domain.donation.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class DonationOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    private DonationStatus status;

    @Column(name = "warehouse_id")
    private UUID warehouseId;

    @Valid
    @NotNull
    @Embedded
    private Donor donor;

    @Valid
    @NotNull
    @OneToMany(mappedBy = "donation", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<DonationItem> items = new ArrayList<>();

    protected DonationOffer() {
    }

    public DonationOffer(Donor donor) {
        this.createdAt = LocalDateTime.now();
        this.status = DonationStatus.PENDING;
        this.donor = donor;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public DonationStatus getStatus() {
        return status;
    }

    public void setStatus(DonationStatus status) {
        this.status = status;
    }

    public UUID getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(UUID warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Donor getDonor() {
        return donor;
    }

    public void setDonor(Donor donor) {
        this.donor = donor;
    }

    public List<DonationItem> getItems() {
        return items;
    }

    public void addItem(DonationItem item) {
        items.add(item);
        item.setDonation(this);
    }

    public void accept(UUID warehouseId) {
        this.status = DonationStatus.ACCEPTED;
        this.warehouseId = warehouseId;
    }

    public void reject() {
        this.status = DonationStatus.REJECTED;
    }

    public void redirect() {
        this.status = DonationStatus.REDIRECTED;
    }
}
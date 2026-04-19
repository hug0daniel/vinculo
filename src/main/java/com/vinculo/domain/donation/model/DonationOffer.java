package com.vinculo.domain.donation.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class DonationOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DonationStatus status;

    @Column(name = "warehouse_id")
    private UUID warehouseId;

    @Embedded
    private Donor donor;

    @OneToMany(mappedBy = "donation", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DonationItem> items = new ArrayList<>();

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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public DonationStatus getStatus() {
        return status;
    }

    public UUID getWarehouseId() {
        return warehouseId;
    }

    public Donor getDonor() {
        return donor;
    }

    public List<DonationItem> getItems() {
        return items;
    }

    public void setStatus(DonationStatus status) {
        this.status = status;
    }

    public void setWarehouseId(UUID warehouseId) {
        this.warehouseId = warehouseId;
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
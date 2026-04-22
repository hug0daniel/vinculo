package com.vinculo.domain.donation.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
public class DonationOffer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DonationStatus status;

    @Setter
    @Column(name = "warehouse_id")
    private UUID warehouseId;

    @Embedded
    private Donor donor;

    @OneToMany(mappedBy = "donation", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<DonationItem> items = new ArrayList<>();

    protected DonationOffer() {
    }

    public DonationOffer(Donor donor) {
        this.createdAt = LocalDateTime.now();
        this.status = DonationStatus.PENDING;
        this.donor = donor;
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
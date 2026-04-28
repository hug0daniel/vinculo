package com.vinculo.domain.inventory.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class StockReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID requestId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lot_id")
    private Lot lot;

    private BigDecimal quantityReserved;

    private LocalDateTime createdAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    protected StockReservation() {
    }

    public StockReservation(UUID requestId, Lot lot, BigDecimal quantityReserved, LocalDateTime expiresAt) {
        this.requestId = requestId;
        this.lot = lot;
        this.quantityReserved = quantityReserved;
        this.createdAt = LocalDateTime.now();
        this.expiresAt = expiresAt;
    }

    public UUID getId() { return id; }
    public UUID getRequestId() { return requestId; }
    public Lot getLot() { return lot; }
    public BigDecimal getQuantityReserved() { return quantityReserved; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public void fulfill() {
        if (lot != null) {
            lot.deduct(quantityReserved);
        }
    }

    public void cancel() {
        this.lot = null;
    }
}
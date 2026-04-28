package com.vinculo.domain.inventory.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Lot {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID productId;

    private String lotNumber;

    private BigDecimal quantity;

    private BigDecimal initialQuantity;

    @Enumerated(EnumType.STRING)
    private Unit unit;

    private LocalDate expiryDate;

    @Column(name = "received_at")
    private LocalDateTime receivedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    protected Lot() {
    }

    public Lot(UUID productId, BigDecimal quantity, Unit unit, LocalDate expiryDate) {
        this.productId = productId;
        this.quantity = quantity;
        this.initialQuantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
        this.receivedAt = LocalDateTime.now();
    }

    public UUID getId() { return id; }
    public UUID getProductId() { return productId; }
    public String getLotNumber() { return lotNumber; }
    public BigDecimal getQuantity() { return quantity; }
    public BigDecimal getInitialQuantity() { return initialQuantity; }
    public Unit getUnit() { return unit; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public LocalDateTime getReceivedAt() { return receivedAt; }
    public Warehouse getWarehouse() { return warehouse; }

    public void setLotNumber(String lotNumber) { this.lotNumber = lotNumber; }

    void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public void deduct(BigDecimal amount) {
        if (amount.compareTo(quantity) > 0) {
            throw new IllegalArgumentException("Insufficient stock in lot");
        }
        this.quantity = this.quantity.subtract(amount);
    }

    public boolean hasStock() {
        return quantity != null && quantity.compareTo(BigDecimal.ZERO) > 0;
    }
}
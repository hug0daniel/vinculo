package com.vinculo.domain.request.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    @Column(name = "disaster_id")
    private UUID disasterId;

    @Column(name = "warehouse_id")
    private UUID warehouseId;

    @Embedded
    private Beneficiary beneficiary;

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<RequestItem> items = new ArrayList<>();

    protected Request() {
    }

    public static Request create(Beneficiary beneficiary, UUID disasterId) {
        Request request = new Request();
        request.beneficiary = beneficiary;
        request.disasterId = disasterId;
        request.createdAt = LocalDateTime.now();
        request.status = RequestStatus.PENDING;
        return request;
    }

    public UUID getId() { return id; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public RequestStatus getStatus() { return status; }
    public UUID getDisasterId() { return disasterId; }
    public UUID getWarehouseId() { return warehouseId; }
    public Beneficiary getBeneficiary() { return beneficiary; }
    public List<RequestItem> getItems() { return items; }

    public void addItem(String productName, BigDecimal quantity, QuantityUnit unit) {
        RequestItem item = new RequestItem(productName, quantity, unit);
        items.add(item);
        item.setRequest(this);
    }

    public void approve(UUID warehouseId) {
        if (this.status != RequestStatus.PENDING) {
            throw new IllegalStateException("Can only approve pending requests");
        }
        this.status = RequestStatus.APPROVED;
        this.warehouseId = warehouseId;
    }

    public void reject() {
        if (this.status != RequestStatus.PENDING) {
            throw new IllegalStateException("Can only reject pending requests");
        }
        this.status = RequestStatus.REJECTED;
    }

    public void fulfill() {
        if (this.status != RequestStatus.APPROVED) {
            throw new IllegalStateException("Can only fulfill approved requests");
        }
        this.status = RequestStatus.FULFILLED;
    }
}

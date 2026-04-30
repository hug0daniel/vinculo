package com.vinculo.domain.request.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
public class RequestItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String productName;
    private BigDecimal quantity;

    @Enumerated(EnumType.STRING)
    private QuantityUnit unit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    private Request request;

    protected RequestItem() {
    }

    public RequestItem(String productName, BigDecimal quantity, QuantityUnit unit) {
        this.productName = productName;
        this.quantity = quantity;
        this.unit = unit;
    }

    public UUID getId() { return id; }
    public String getProductName() { return productName; }
    public BigDecimal getQuantity() { return quantity; }
    public QuantityUnit getUnit() { return unit; }
    public Request getRequest() { return request; }

    void setRequest(Request request) {
        this.request = request;
    }
}

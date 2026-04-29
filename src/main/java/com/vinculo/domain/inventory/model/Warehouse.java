package com.vinculo.domain.inventory.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @Enumerated(EnumType.STRING)
    private WarehouseType type;

    @Enumerated(EnumType.STRING)
    private WarehouseStatus status;

    @Embedded
    private Location location;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Lot> lots = new ArrayList<>();

    protected Warehouse() {
    }

    private Warehouse(Builder builder) {
        this.name = builder.name;
        this.type = builder.type;
        this.status = builder.status != null ? builder.status : WarehouseStatus.ACTIVE;
        this.location = builder.location;
        this.createdAt = LocalDateTime.now();
    }

    public static Builder builder() {
        return new Builder();
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public WarehouseType getType() { return type; }
    public WarehouseStatus getStatus() { return status; }
    public Location getLocation() { return location; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public List<Lot> getLots() { return lots; }

    public void addLot(Lot lot) {
        lots.add(lot);
        lot.setWarehouse(this);
    }

    public void deactivate() {
        if (this.status == WarehouseStatus.ACTIVE) {
            this.status = WarehouseStatus.INACTIVE;
        }
    }

    public void activate() {
        if (this.status == WarehouseStatus.INACTIVE) {
            this.status = WarehouseStatus.ACTIVE;
        }
    }

    public static class Builder {
        private String name;
        private WarehouseType type;
        private WarehouseStatus status;
        private Location location;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder type(WarehouseType type) {
            this.type = type;
            return this;
        }

        public Builder status(WarehouseStatus status) {
            this.status = status;
            return this;
        }

        public Builder location(Location location) {
            this.location = location;
            return this;
        }

        public Warehouse build() {
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Warehouse name is required");
            }
            if (type == null) {
                throw new IllegalArgumentException("Warehouse type is required");
            }
            return new Warehouse(this);
        }
    }
}
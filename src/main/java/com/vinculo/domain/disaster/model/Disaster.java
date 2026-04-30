package com.vinculo.domain.disaster.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Disaster {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @Enumerated(EnumType.STRING)
    private DisasterType type;

    @Enumerated(EnumType.STRING)
    private DisasterStatus status;

    private String location;

    private LocalDate startDate;

    private LocalDate endDate;

    protected Disaster() {
    }

    public static Disaster create(String name, DisasterType type, String location) {
        Disaster disaster = new Disaster();
        disaster.name = name;
        disaster.type = type;
        disaster.location = location;
        disaster.status = DisasterStatus.ACTIVE;
        disaster.startDate = LocalDate.now();
        return disaster;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public DisasterType getType() { return type; }
    public DisasterStatus getStatus() { return status; }
    public String getLocation() { return location; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }

    public void updateDetails(String name, DisasterType type, String location) {
        this.name = name;
        this.type = type;
        this.location = location;
    }

    public void deactivate() {
        if (this.status != DisasterStatus.ACTIVE) {
            throw new IllegalStateException("Can only deactivate active disasters");
        }
        this.status = DisasterStatus.INACTIVE;
        this.endDate = LocalDate.now();
    }

    public void reactivate() {
        if (this.status != DisasterStatus.INACTIVE) {
            throw new IllegalStateException("Can only reactivate inactive disasters");
        }
        this.status = DisasterStatus.ACTIVE;
        this.endDate = null;
    }
}

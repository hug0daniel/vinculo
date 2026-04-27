package com.vinculo.domain.user.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Partner {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String organizationName;

    @Enumerated(EnumType.STRING)
    private PartnerType type;

    @Column(name = "contact")
    private String contact;

    @Column(name = "zone")
    private String zone;

    @OneToMany(mappedBy = "partner")
    private List<User> users = new ArrayList<>();

    protected Partner() {
    }

    public Partner(String organizationName, PartnerType type, String contact, String zone) {
        this.organizationName = organizationName;
        this.type = type;
        this.contact = contact;
        this.zone = zone;
    }

    public UUID getId() { return id; }
    public String getOrganizationName() { return organizationName; }
    public PartnerType getType() { return type; }
    public String getContact() { return contact; }
    public String getZone() { return zone; }
    public List<User> getUsers() { return users; }

    public void setOrganizationName(String organizationName) { this.organizationName = organizationName; }
    public void setType(PartnerType type) { this.type = type; }
    public void setContact(String contact) { this.contact = contact; }
    public void setZone(String zone) { this.zone = zone; }

    public void addUser(User user) {
        users.add(user);
        user.setPartner(this);
    }
}
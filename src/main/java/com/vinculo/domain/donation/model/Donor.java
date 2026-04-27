package com.vinculo.domain.donation.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Donor {

    private String name;
    private String contact;
    private DonorType type;

    protected Donor() {
    }

    public Donor(String name, String contact, DonorType type) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Donor name is required");
        }
        if (contact == null || contact.isBlank()) {
            throw new IllegalArgumentException("Donor contact is required");
        }
        this.name = name;
        this.contact = contact;
        this.type = type;
    }

    public String getName() { return name; }
    public String getContact() { return contact; }
    public DonorType getType() { return type; }
}
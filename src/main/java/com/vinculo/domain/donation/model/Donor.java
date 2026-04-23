package com.vinculo.domain.donation.model;

import jakarta.persistence.Embeddable;

@Embeddable
public record Donor(
    String name,
    String contact,
    DonorType type
) {
    public Donor {
        if (name == null || name.isBlank()) {
            name = null;
        }
        if (contact == null || contact.isBlank()) {
            contact = null;
        }
    }
}
package com.vinculo.domain.donation.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Embeddable
public record Donor(
    @Size(max = 200)
    String name,
    @Email
    String contact,
    @NotNull
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
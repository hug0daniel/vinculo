package com.vinculo.unit.domain.donation.model;

import com.vinculo.domain.donation.model.Donor;
import com.vinculo.domain.donation.model.DonorType;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DonorTest {

    @Test
    void shouldCreateDonor() {
        var donor = new Donor("John Doe", "john@example.com", DonorType.INDIVIDUAL);
        
        assertEquals("John Doe", donor.getName());
        assertEquals("john@example.com", donor.getContact());
        assertEquals(DonorType.INDIVIDUAL, donor.getType());
    }

    @Test
    void shouldFailWhenNameIsMissing() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Donor("", "john@example.com", DonorType.INDIVIDUAL)
        );
    }

    @Test
    void shouldFailWhenContactIsMissing() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Donor("John Doe", "", DonorType.INDIVIDUAL)
        );
    }

    @Test
    void shouldFailWhenDonorIsNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            new Donor(null, null, DonorType.INDIVIDUAL)
        );
    }
}
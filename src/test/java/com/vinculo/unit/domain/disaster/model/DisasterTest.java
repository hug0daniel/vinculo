package com.vinculo.unit.domain.disaster.model;

import com.vinculo.domain.disaster.model.Disaster;
import com.vinculo.domain.disaster.model.DisasterStatus;
import com.vinculo.domain.disaster.model.DisasterType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DisasterTest {

    @Nested
    @DisplayName("Creation")
    class CreationTests {

        @Test
        @DisplayName("should create disaster with active status")
        void shouldCreateWithActiveStatus() {
            Disaster disaster = Disaster.create("Enchente SP 2026", DisasterType.FLOOD, "São Paulo");

            assertEquals("Enchente SP 2026", disaster.getName());
            assertEquals(DisasterType.FLOOD, disaster.getType());
            assertEquals(DisasterStatus.ACTIVE, disaster.getStatus());
            assertNotNull(disaster.getStartDate());
        }
    }

    @Nested
    @DisplayName("Update Details")
    class UpdateDetailsTests {

        @Test
        @DisplayName("should update disaster details")
        void shouldUpdateDetails() {
            Disaster disaster = Disaster.create("Old Name", DisasterType.FIRE, "Old Location");

            disaster.updateDetails("New Name", DisasterType.EARTHQUAKE, "New Location");

            assertEquals("New Name", disaster.getName());
            assertEquals(DisasterType.EARTHQUAKE, disaster.getType());
            assertEquals("New Location", disaster.getLocation());
        }
    }

    @Nested
    @DisplayName("Deactivate Transition")
    class DeactivateTransitionTests {

        @Test
        @DisplayName("should deactivate active disaster")
        void shouldDeactivateActiveDisaster() {
            Disaster disaster = Disaster.create("Test Disaster", DisasterType.FLOOD, "Test Location");

            disaster.deactivate();

            assertEquals(DisasterStatus.INACTIVE, disaster.getStatus());
            assertNotNull(disaster.getEndDate());
        }

        @Test
        @DisplayName("should throw when deactivating non-active disaster")
        void shouldThrowWhenNotActive() {
            Disaster disaster = Disaster.create("Test Disaster", DisasterType.FLOOD, "Test Location");
            disaster.deactivate();

            assertThrows(IllegalStateException.class, disaster::deactivate);
        }
    }

    @Nested
    @DisplayName("Reactivate Transition")
    class ReactivateTransitionTests {

        @Test
        @DisplayName("should reactivate inactive disaster")
        void shouldReactivateInactiveDisaster() {
            Disaster disaster = Disaster.create("Test Disaster", DisasterType.FLOOD, "Test Location");
            disaster.deactivate();

            disaster.reactivate();

            assertEquals(DisasterStatus.ACTIVE, disaster.getStatus());
            assertNull(disaster.getEndDate());
        }

        @Test
        @DisplayName("should throw when reactivating non-inactive disaster")
        void shouldThrowWhenNotInactive() {
            Disaster disaster = Disaster.create("Test Disaster", DisasterType.FLOOD, "Test Location");

            assertThrows(IllegalStateException.class, disaster::reactivate);
        }
    }
}

package com.vinculo.integration.domain.disaster.persistence;

import com.vinculo.domain.disaster.model.Disaster;
import com.vinculo.domain.disaster.model.DisasterStatus;
import com.vinculo.domain.disaster.model.DisasterType;
import com.vinculo.domain.disaster.repository.DisasterRepository;
import com.vinculo.VinculoApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = VinculoApplication.class)
@ActiveProfiles("test")
@Transactional
@DisplayName("Disaster Persistence")
class DisasterPersistenceTest {

    @Autowired
    private DisasterRepository disasterRepository;

    @Test
    @DisplayName("should save and retrieve disaster")
    void shouldSaveAndRetrieveDisaster() {
        Disaster disaster = Disaster.create("Enchente SP 2026", DisasterType.FLOOD, "São Paulo");

        Disaster saved = disasterRepository.save(disaster);

        Optional<Disaster> found = disasterRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("Enchente SP 2026", found.get().getName());
        assertEquals(DisasterStatus.ACTIVE, found.get().getStatus());
    }

    @Test
    @DisplayName("should update disaster details")
    void shouldUpdateDisasterDetails() {
        Disaster disaster = Disaster.create("Old Name", DisasterType.FIRE, "Old Location");
        Disaster saved = disasterRepository.save(disaster);

        saved.updateDetails("New Name", DisasterType.EARTHQUAKE, "New Location");

        Optional<Disaster> found = disasterRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("New Name", found.get().getName());
        assertEquals(DisasterType.EARTHQUAKE, found.get().getType());
    }

    @Test
    @DisplayName("should deactivate disaster")
    void shouldDeactivateDisaster() {
        Disaster disaster = Disaster.create("Test Disaster", DisasterType.FLOOD, "Test Location");
        Disaster saved = disasterRepository.save(disaster);

        saved.deactivate();

        Optional<Disaster> found = disasterRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals(DisasterStatus.INACTIVE, found.get().getStatus());
        assertNotNull(found.get().getEndDate());
    }

    @Test
    @DisplayName("should find all disasters")
    void shouldFindAllDisasters() {
        Disaster disaster1 = Disaster.create("Disaster 1", DisasterType.FLOOD, "Location 1");
        Disaster disaster2 = Disaster.create("Disaster 2", DisasterType.FIRE, "Location 2");

        disasterRepository.save(disaster1);
        disasterRepository.save(disaster2);

        List<Disaster> disasters = disasterRepository.findAll();

        assertTrue(disasters.size() >= 2);
    }
}

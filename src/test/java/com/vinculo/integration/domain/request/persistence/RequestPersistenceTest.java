package com.vinculo.integration.domain.request.persistence;

import com.vinculo.domain.request.model.Beneficiary;
import com.vinculo.domain.request.model.QuantityUnit;
import com.vinculo.domain.request.model.Request;
import com.vinculo.domain.request.model.RequestStatus;
import com.vinculo.domain.request.repository.RequestRepository;
import com.vinculo.VinculoApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = VinculoApplication.class)
@ActiveProfiles("test")
@Transactional
@DisplayName("Request Persistence")
class RequestPersistenceTest {

    @Autowired
    private RequestRepository requestRepository;

    @Test
    @DisplayName("should save and retrieve request")
    void shouldSaveAndRetrieveRequest() {
        Beneficiary beneficiary = new Beneficiary("John Doe", "john@test.com", "DOC-001");
        Request request = Request.create(beneficiary, UUID.randomUUID());
        request.addItem("Rice", new BigDecimal("50"), QuantityUnit.KG);

        Request saved = requestRepository.save(request);

        Optional<Request> found = requestRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals("John Doe", found.get().getBeneficiary().getName());
        assertEquals(RequestStatus.PENDING, found.get().getStatus());
        assertEquals(1, found.get().getItems().size());
    }

    @Test
    @DisplayName("should update request status")
    void shouldUpdateRequestStatus() {
        Beneficiary beneficiary = new Beneficiary("John Doe", "john@test.com", "DOC-001");
        Request request = Request.create(beneficiary, UUID.randomUUID());
        request.addItem("Rice", new BigDecimal("50"), QuantityUnit.KG);

        Request saved = requestRepository.save(request);

        saved.approve(UUID.randomUUID());
        requestRepository.save(saved);

        Request found = requestRepository.findById(saved.getId()).orElse(null);

        assertNotNull(found);
        assertEquals(RequestStatus.APPROVED, found.getStatus());
    }

    @Test
    @DisplayName("should find all requests")
    void shouldFindAllRequests() {
        Beneficiary beneficiary = new Beneficiary("John Doe", "john@test.com", "DOC-001");
        Request request1 = Request.create(beneficiary, UUID.randomUUID());
        Request request2 = Request.create(beneficiary, UUID.randomUUID());

        requestRepository.save(request1);
        requestRepository.save(request2);

        List<Request> requests = requestRepository.findAll();

        assertTrue(requests.size() >= 2);
    }
}

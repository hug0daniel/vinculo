package com.vinculo.unit.domain.request.model;

import com.vinculo.domain.request.model.Beneficiary;
import com.vinculo.domain.request.model.QuantityUnit;
import com.vinculo.domain.request.model.Request;
import com.vinculo.domain.request.model.RequestItem;
import com.vinculo.domain.request.model.RequestStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RequestTest {

    private final Beneficiary beneficiary = new Beneficiary("John Doe", "john@test.com", "DOC-001");

    @Nested
    @DisplayName("Creation")
    class CreationTests {

        @Test
        @DisplayName("should create request with pending status")
        void shouldCreateWithPendingStatus() {
            Request request = Request.create(beneficiary, UUID.randomUUID());

            assertEquals(RequestStatus.PENDING, request.getStatus());
            assertNotNull(request.getCreatedAt());
            assertEquals(beneficiary.getName(), request.getBeneficiary().getName());
        }
    }

    @Nested
    @DisplayName("Add Items")
    class AddItemsTests {

        @Test
        @DisplayName("should add item to request")
        void shouldAddItem() {
            Request request = Request.create(beneficiary, UUID.randomUUID());

            request.addItem("Rice", new BigDecimal("50"), QuantityUnit.KG);

            assertEquals(1, request.getItems().size());
            assertEquals("Rice", request.getItems().getFirst().getProductName());
        }
    }

    @Nested
    @DisplayName("Approve Transition")
    class ApproveTransitionTests {

        @Test
        @DisplayName("should approve pending request")
        void shouldApprovePendingRequest() {
            Request request = createPendingRequest();

            request.approve(UUID.randomUUID());

            assertEquals(RequestStatus.APPROVED, request.getStatus());
        }

        @Test
        @DisplayName("should throw when approving non-pending request")
void shouldThrowWhenNotPending() {
            Request request = createPendingRequest();
            request.approve(UUID.randomUUID());

            assertThrows(IllegalStateException.class, request::reject);
        }
    }

    @Nested
    @DisplayName("Reject Transition")
    class RejectTransitionTests {

        @Test
        @DisplayName("should reject pending request")
        void shouldRejectPendingRequest() {
            Request request = createPendingRequest();

            request.reject();

            assertEquals(RequestStatus.REJECTED, request.getStatus());
        }

        @Test
        @DisplayName("should throw when rejecting non-pending request")
        void shouldThrowWhenNotPending() {
            Request request = createPendingRequest();
            request.approve(UUID.randomUUID());

            assertThrows(IllegalStateException.class, request::reject);
        }
    }

    @Nested
    @DisplayName("Fulfill Transition")
    class FulfillTransitionTests {

        @Test
        @DisplayName("should fulfill approved request")
        void shouldFulfillApprovedRequest() {
            Request request = createPendingRequest();
            request.approve(UUID.randomUUID());

            request.fulfill();

            assertEquals(RequestStatus.FULFILLED, request.getStatus());
        }

        @Test
        @DisplayName("should throw when fulfilling non-approved request")
        void shouldThrowWhenNotApproved() {
            Request request = createPendingRequest();

            assertThrows(IllegalStateException.class, request::fulfill);
        }
    }

    @Nested
    @DisplayName("RequestItem")
    class RequestItemTests {

        @Test
        @DisplayName("should create request item with correct values")
        void shouldCreateWithCorrectValues() {
            RequestItem item = new RequestItem("Beans", new BigDecimal("30"), QuantityUnit.KG);

            assertEquals("Beans", item.getProductName());
            assertEquals(new BigDecimal("30"), item.getQuantity());
            assertEquals(QuantityUnit.KG, item.getUnit());
        }
    }

    private Request createPendingRequest() {
        Request request = Request.create(beneficiary, UUID.randomUUID());
        request.addItem("Rice", new BigDecimal("50"), QuantityUnit.KG);
        return request;
    }
}

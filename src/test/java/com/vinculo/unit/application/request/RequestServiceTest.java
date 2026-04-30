package com.vinculo.unit.application.request;

import com.vinculo.api.request.dto.BeneficiaryDto;
import com.vinculo.api.request.dto.CreateRequestRequest;
import com.vinculo.api.request.dto.ItemDto;
import com.vinculo.api.request.dto.RequestItemDto;
import com.vinculo.api.request.dto.RequestListItem;
import com.vinculo.api.request.dto.RequestResponse;
import com.vinculo.api.request.mapper.RequestMapper;
import com.vinculo.application.exception.ResourceNotFoundException;
import com.vinculo.application.request.RequestService;
import com.vinculo.application.request.RequestServiceImpl;
import com.vinculo.domain.request.model.Beneficiary;
import com.vinculo.domain.request.model.QuantityUnit;
import com.vinculo.domain.request.model.Request;
import com.vinculo.domain.request.repository.RequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RequestService")
class RequestServiceTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private RequestMapper mapper;

    @InjectMocks
    private RequestServiceImpl requestService;

    private UUID requestId;
    private CreateRequestRequest createRequest;
    private RequestResponse mockResponse;
    private RequestListItem mockListItem;

    @BeforeEach
    void setUp() {
        requestId = UUID.randomUUID();

        createRequest = new CreateRequestRequest(
                new BeneficiaryDto("John Doe", "john@test.com", "DOC-001"),
                UUID.randomUUID(),
                List.of(new RequestItemDto("Rice", new BigDecimal("50"), QuantityUnit.KG))
        );

        mockResponse = new RequestResponse(
                requestId, null, null, null, null, null
        );

        mockListItem = new RequestListItem(
                requestId, null, null, null, "John Doe", 1
        );
    }

    @Nested
    @DisplayName("Create Request")
    class CreateRequestTests {

        @Test
        @DisplayName("should create request successfully")
        void shouldCreateRequest() {
            Request savedRequest = mock(Request.class);
            when(requestRepository.save(any(Request.class))).thenReturn(savedRequest);
            when(mapper.toResponse(any(Request.class))).thenReturn(mockResponse);

            RequestResponse result = requestService.createRequest(createRequest);

            assertNotNull(result);
            verify(requestRepository, times(1)).save(any(Request.class));
        }
    }

    @Nested
    @DisplayName("Approve Request")
    class ApproveRequestTests {

        @Test
        @DisplayName("should approve pending request")
        void shouldApproveRequest() {
            Request request = mock(Request.class);
            when(requestRepository.findById(requestId)).thenReturn(Optional.of(request));
            when(mapper.toResponse(request)).thenReturn(mockResponse);

            RequestResponse result = requestService.approveRequest(requestId, UUID.randomUUID());

            assertNotNull(result);
            verify(request, times(1)).approve(any(UUID.class));
        }

        @Test
        @DisplayName("should throw when request not found")
        void shouldThrowWhenNotFound() {
            when(requestRepository.findById(requestId)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> requestService.approveRequest(requestId, UUID.randomUUID()));
        }
    }

    @Nested
    @DisplayName("Get Requests")
    class GetRequestsTests {

        @Test
        @DisplayName("should return list of requests")
        void shouldReturnRequestsList() {
            Request request = mock(Request.class);
            when(requestRepository.findAll()).thenReturn(List.of(request));
            when(mapper.toListItem(any(Request.class))).thenReturn(mockListItem);

            List<RequestListItem> result = requestService.getRequests();

            assertEquals(1, result.size());
        }
    }
}

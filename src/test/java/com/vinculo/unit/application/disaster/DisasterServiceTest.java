package com.vinculo.unit.application.disaster;

import com.vinculo.api.disaster.dto.CreateDisasterRequest;
import com.vinculo.api.disaster.dto.DisasterItem;
import com.vinculo.api.disaster.dto.DisasterResponse;
import com.vinculo.api.disaster.dto.UpdateDisasterRequest;
import com.vinculo.api.disaster.mapper.DisasterMapper;
import com.vinculo.application.disaster.DisasterServiceImpl;
import com.vinculo.application.exception.ResourceNotFoundException;
import com.vinculo.domain.disaster.model.Disaster;
import com.vinculo.domain.disaster.model.DisasterType;
import com.vinculo.domain.disaster.repository.DisasterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DisasterService")
class DisasterServiceTest {

    @Mock
    private DisasterRepository disasterRepository;

    @Mock
    private DisasterMapper mapper;

    @InjectMocks
    private DisasterServiceImpl disasterService;

    private UUID disasterId;
    private CreateDisasterRequest createRequest;
    private UpdateDisasterRequest updateRequest;
    private DisasterResponse mockResponse;
    private DisasterItem mockListItem;

    @BeforeEach
    void setUp() {
        disasterId = UUID.randomUUID();

        createRequest = new CreateDisasterRequest(
                "Enchente SP 2026",
                DisasterType.FLOOD,
                "São Paulo"
        );

        updateRequest = new UpdateDisasterRequest(
                "Updated Name",
                DisasterType.EARTHQUAKE,
                "Updated Location"
        );

        mockResponse = new DisasterResponse(
                disasterId, "Test", DisasterType.FLOOD, null, "Location", null, null
        );

        mockListItem = new DisasterItem(
                disasterId, "Test", DisasterType.FLOOD, null, "Location"
        );
    }

    @Nested
    @DisplayName("Create Disaster")
    class CreateDisasterTests {

        @Test
        @DisplayName("should create disaster successfully")
        void shouldCreateDisaster() {
            Disaster disaster = Disaster.create("Enchente SP 2026", DisasterType.FLOOD, "São Paulo");
            when(disasterRepository.save(any(Disaster.class))).thenReturn(disaster);
            when(mapper.toEntity(any(CreateDisasterRequest.class))).thenReturn(disaster);
            when(mapper.toResponse(any(Disaster.class))).thenReturn(mockResponse);

            DisasterResponse result = disasterService.createDisaster(createRequest);

            assertNotNull(result);
            verify(disasterRepository, times(1)).save(any(Disaster.class));
        }
    }

    @Nested
    @DisplayName("Update Disaster")
    class UpdateDisasterTests {

        @Test
        @DisplayName("should update disaster details")
        void shouldUpdateDisaster() {
            Disaster disaster = mock(Disaster.class);
            when(disasterRepository.findById(disasterId)).thenReturn(Optional.of(disaster));
            when(mapper.toResponse(disaster)).thenReturn(mockResponse);

            DisasterResponse result = disasterService.updateDisaster(disasterId, updateRequest);

            assertNotNull(result);
            verify(disaster, times(1)).updateDetails(updateRequest.name(), updateRequest.type(), updateRequest.location());
        }

        @Test
        @DisplayName("should throw when disaster not found")
        void shouldThrowWhenNotFound() {
            when(disasterRepository.findById(disasterId)).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> disasterService.updateDisaster(disasterId, updateRequest));
        }
    }

    @Nested
    @DisplayName("Get Disasters")
    class GetDisastersTests {

        @Test
        @DisplayName("should return list of disasters")
        void shouldReturnDisastersList() {
            Disaster disaster = mock(Disaster.class);
            when(disasterRepository.findAll()).thenReturn(List.of(disaster));
            when(mapper.toListItem(disaster)).thenReturn(mockListItem);

            List<DisasterItem> result = disasterService.getDisasters();

            assertEquals(1, result.size());
        }
    }
}

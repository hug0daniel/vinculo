package com.vinculo.unit.application.donation;

import com.vinculo.api.donation.dto.DonationRequest;
import com.vinculo.api.donation.dto.DonationResponse;
import com.vinculo.api.donation.utils.DonationMapper;
import com.vinculo.api.warehouse.dto.LotRequest;
import com.vinculo.application.inventory.StockManagementPort;
import com.vinculo.domain.donation.model.DonationOffer;
import com.vinculo.domain.donation.model.DonationStatus;
import com.vinculo.domain.donation.model.Donor;
import com.vinculo.domain.donation.model.DonorType;
import com.vinculo.domain.donation.model.QuantityUnit;
import com.vinculo.domain.donation.repository.DonationRepository;
import com.vinculo.domain.inventory.model.Warehouse;
import com.vinculo.domain.inventory.model.WarehouseType;
import com.vinculo.domain.inventory.repository.WarehouseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DonationServiceTest {

    @Mock
    private DonationRepository donationRepository;

    @Mock
    private DonationMapper mapper;

    @Mock
    private StockManagementPort stockManagementPort;

    @Mock
    private WarehouseRepository warehouseRepository;

    private com.vinculo.application.donation.DonationServiceImpl donationService;

    private UUID donationId;
    private UUID warehouseId;

    @BeforeEach
    void setUp() {
        donationService = new com.vinculo.application.donation.DonationServiceImpl(
                donationRepository, mapper, stockManagementPort, warehouseRepository);
        donationId = UUID.randomUUID();
        warehouseId = UUID.randomUUID();
    }

    @Nested
    @DisplayName("Create Donation")
    class CreateDonationTests {

        @Test
        @DisplayName("should create donation with pending status")
        void shouldCreateDonation() {
            var donor = new Donor("John Doe", "john@example.com", DonorType.INDIVIDUAL);
            var donation = DonationOffer.builder().donor(donor).build();

            var request = new DonationRequest(
                    new DonationRequest.DonorDto("John Doe", "john@example.com", DonorType.INDIVIDUAL),
                    List.of(new DonationRequest.DonationItemDto("", new BigDecimal("50"), QuantityUnit.KG, LocalDate.now().plusMonths(6)))
            );

            when(mapper.toEntity(request)).thenReturn(donation);
            when(donationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
            when(mapper.toResponse(any())).thenReturn(new DonationResponse(UUID.randomUUID(), DonationStatus.PENDING, null, null, List.of()));

            var result = donationService.createDonation(request);

            assertNotNull(result);
            verify(donationRepository).save(any());
        }
    }

    @Nested
    @DisplayName("Accept Donation")
    class AcceptDonationTests {

        @Test
        @DisplayName("should accept pending donation and create lots via port")
        void shouldAcceptDonation() {
            var donation = DonationOffer.builder()
                    .donor(new Donor("John Doe", "john@example.com", DonorType.INDIVIDUAL))
                    .build();

            var warehouse = Warehouse.builder()
                    .name("Test Warehouse")
                    .type(WarehouseType.WAREHOUSE)
                    .status(com.vinculo.domain.inventory.model.WarehouseStatus.ACTIVE)
                    .build();

            String productName = "Rice";
            donation.addItem(productName, new BigDecimal("50"), QuantityUnit.KG, LocalDate.now().plusMonths(6));

            when(donationRepository.findById(donationId)).thenReturn(Optional.of(donation));
            when(warehouseRepository.findById(warehouseId)).thenReturn(Optional.of(warehouse));
            when(mapper.toResponse(any())).thenReturn(new DonationResponse(donationId, DonationStatus.ACCEPTED, null, null, List.of()));

            var result = donationService.acceptDonation(donationId, warehouseId);

            assertEquals(DonationStatus.ACCEPTED, result.status());
            verify(stockManagementPort).createLotInWarehouse(eq(warehouseId), any(LotRequest.class));
        }

        @Test
        @DisplayName("should fail when warehouse is inactive")
        void shouldFailWhenWarehouseInactive() {
            var donation = DonationOffer.builder()
                    .donor(new Donor("John Doe", "john@example.com", DonorType.INDIVIDUAL))
                    .build();

            var warehouse = Warehouse.builder()
                    .name("Test Warehouse")
                    .type(WarehouseType.WAREHOUSE)
                    .status(com.vinculo.domain.inventory.model.WarehouseStatus.INACTIVE)
                    .build();

            when(donationRepository.findById(donationId)).thenReturn(Optional.of(donation));
            when(warehouseRepository.findById(warehouseId)).thenReturn(Optional.of(warehouse));

            assertThrows(IllegalStateException.class, () ->
                    donationService.acceptDonation(donationId, warehouseId));
        }
    }

    @Nested
    @DisplayName("Reject Donation")
    class RejectDonationTests {

        @Test
        @DisplayName("should reject pending donation")
        void shouldRejectDonation() {
            var donation = DonationOffer.builder()
                    .donor(new Donor("John Doe", "john@example.com", DonorType.INDIVIDUAL))
                    .build();

            when(donationRepository.findById(donationId)).thenReturn(Optional.of(donation));
            when(mapper.toResponse(any())).thenReturn(new DonationResponse(donationId, DonationStatus.REJECTED, null, null, List.of()));

            var result = donationService.rejectDonation(donationId);

            assertEquals(DonationStatus.REJECTED, result.status());
        }
    }

    @Nested
    @DisplayName("Get Donation")
    class GetDonationTests {

        @Test
        @DisplayName("should get donation by id")
        void shouldGetDonation() {
            var donation = DonationOffer.builder()
                    .donor(new Donor("John Doe", "john@example.com", DonorType.INDIVIDUAL))
                    .build();

            var response = new DonationResponse(donationId, DonationStatus.PENDING, null, null, List.of());
            when(donationRepository.findById(donationId)).thenReturn(Optional.of(donation));
            when(mapper.toResponse(any())).thenReturn(response);

            var result = donationService.getDonation(donationId);

            assertNotNull(result);
            assertEquals(donationId, result.donationId());
            verify(donationRepository).findById(donationId);
        }
    }
}
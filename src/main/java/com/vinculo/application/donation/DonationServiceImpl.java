package com.vinculo.application.donation;

import com.vinculo.api.donation.dto.DonationRequest;
import com.vinculo.api.donation.dto.DonationResponse;
import com.vinculo.api.donation.utils.DonationMapper;
import com.vinculo.api.lot.controller.dto.LotRequest;
import com.vinculo.application.exception.ResourceNotFoundException;
import com.vinculo.application.inventory.StockManagementPort;
import com.vinculo.domain.donation.model.DonationItem;
import com.vinculo.domain.donation.model.DonationOffer;
import com.vinculo.domain.donation.model.DonationStatus;
import com.vinculo.domain.donation.repository.DonationRepository;
import com.vinculo.domain.inventory.model.Unit;
import com.vinculo.domain.inventory.model.Warehouse;
import com.vinculo.domain.inventory.model.WarehouseStatus;
import com.vinculo.domain.inventory.repository.WarehouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class DonationServiceImpl implements DonationService {

    private final DonationRepository donationRepository;
    private final DonationMapper mapper;
    private final StockManagementPort stockManagementPort;
    private final WarehouseRepository warehouseRepository;

    public DonationServiceImpl(DonationRepository donationRepository,
                         DonationMapper mapper,
                         StockManagementPort stockManagementPort,
                         WarehouseRepository warehouseRepository) {
        this.donationRepository = donationRepository;
        this.mapper = mapper;
        this.stockManagementPort = stockManagementPort;
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public DonationResponse createDonation(DonationRequest request) {
        DonationOffer donation = mapper.toEntity(request);
        DonationOffer saved = donationRepository.save(donation);
        return mapper.toResponse(saved);
    }

    @Override
    public DonationResponse acceptDonation(UUID donationId, UUID warehouseId) {
        DonationOffer donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new ResourceNotFoundException("Donation not found"));

        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found: " + warehouseId));

        if (warehouse.getStatus() != WarehouseStatus.ACTIVE) {
            throw new IllegalStateException("Warehouse is not active");
        }

        for (DonationItem item : donation.getItems()) {
            LotRequest request = new LotRequest(
                        item.getProductName(),
                        item.getQuantity(),
                        mapQuantityToUnit(item.getUnit()),
                        item.getExpiryDate(),
                        null
                );
                stockManagementPort.createLotInWarehouse(warehouseId, request);
        }

        donation.accept(warehouseId);
        return mapper.toResponse(donation);
    }

    @Override
    public DonationResponse rejectDonation(UUID donationId) {
        DonationOffer donation = donationRepository.findById(donationId)
                .orElseThrow(() -> new ResourceNotFoundException("Donation not found"));

        donation.reject();
        return mapper.toResponse(donation);
    }

    @Override
    public DonationResponse getDonation(UUID id) {
        DonationOffer donation = donationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Donation not found"));
        return mapper.toResponse(donation);
    }

    @Override
    public List<DonationResponse> getDonations(DonationStatus status) {
        if (status == null) {
            return donationRepository.findAll().stream()
                    .map(mapper::toResponse)
                    .toList();
        }
        return donationRepository.findByStatus(status).stream()
                .map(mapper::toResponse)
                .toList();
    }

    private Unit mapQuantityToUnit(com.vinculo.domain.donation.model.QuantityUnit quantityUnit) {
        return switch (quantityUnit) {
            case KG -> Unit.KG;
            case LITER -> Unit.LITER;
            case UNIT -> Unit.UNIT;
        };
    }
}
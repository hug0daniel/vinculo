package com.vinculo.application.inventory;

import com.vinculo.domain.inventory.model.Lot;
import com.vinculo.domain.inventory.model.StockReservation;
import com.vinculo.domain.inventory.repository.LotRepository;
import com.vinculo.domain.inventory.repository.StockReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static java.math.BigDecimal.ZERO;

@Service
public class StockReservationService {

    private final StockReservationRepository reservationRepository;
    private final LotRepository lotRepository;
    private final FefoStockService fefoStockService;

    private static final int RESERVATION_TTL_HOURS = 24;

    public StockReservationService(StockReservationRepository reservationRepository,
                            LotRepository lotRepository,
                            FefoStockService fefoStockService) {
        this.reservationRepository = reservationRepository;
        this.lotRepository = lotRepository;
        this.fefoStockService = fefoStockService;
    }

    @Transactional
    public ReservationResult reserveStock(UUID requestId, UUID warehouseId, UUID productId, BigDecimal quantity) {
        List<Lot> lots = lotRepository.findByWarehouse_IdAndProductIdAndQuantityGreaterThanOrderByExpiryDateAsc(
                warehouseId, productId, ZERO);

        if (lots.isEmpty()) {
            throw new IllegalArgumentException("No stock available for product: " + productId);
        }

        BigDecimal available = fefoStockService.getTotalAvailableStock(lots);
        if (available.compareTo(quantity) < 0) {
            throw new IllegalArgumentException("Insufficient stock. Available: " + available + ", Requested: " + quantity);
        }

        List<FefoStockService.LotAllocation> allocations = fefoStockService.allocateStock(productId, quantity, lots);

        LocalDateTime expiresAt = LocalDateTime.now().plusHours(RESERVATION_TTL_HOURS);

        for (var allocation : allocations) {
            StockReservation reservation = new StockReservation(
                    requestId,
                    allocation.lot(),
                    allocation.quantity(),
                    expiresAt
            );
            reservationRepository.save(reservation);
        }

        return new ReservationResult(requestId, allocations.size(), expiresAt);
    }

    @Transactional
    public void fulfillReservation(UUID requestId) {
        List<StockReservation> reservations = reservationRepository.findByRequestId(requestId);

        for (var reservation : reservations) {
            reservation.fulfill();
        }

        reservationRepository.deleteByRequestId(requestId);
    }

    @Transactional
    public void cancelReservation(UUID requestId) {
        List<StockReservation> reservations = reservationRepository.findByRequestId(requestId);

        for (var reservation : reservations) {
            reservation.cancel();
        }

        reservationRepository.deleteByRequestId(requestId);
    }

    public void expireReservations() {
        List<StockReservation> expired = reservationRepository.findAll().stream()
                .filter(StockReservation::isExpired)
                .toList();

        for (var reservation : expired) {
            cancelReservation(reservation.getId());
        }
    }

    public record ReservationResult(UUID requestId, int lotsReserved, LocalDateTime expiresAt) {}
}
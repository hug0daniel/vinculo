package com.vinculo.domain.inventory.repository;

import com.vinculo.domain.inventory.model.Lot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Repository
public interface LotRepository extends JpaRepository<Lot, UUID> {

    List<Lot> findByWarehouseId(UUID warehouseId);

    List<Lot> findByWarehouse_IdOrderByExpiryDateAsc(UUID warehouseId);

    List<Lot> findByProductIdAndQuantityGreaterThanOrderByExpiryDateAsc(UUID productId, BigDecimal zero);

    List<Lot> findByWarehouse_IdAndProductIdAndQuantityGreaterThanOrderByExpiryDateAsc(
            UUID warehouseId, UUID productId, BigDecimal zero);
}
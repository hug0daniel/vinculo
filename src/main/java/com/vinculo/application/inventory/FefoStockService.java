package com.vinculo.application.inventory;

import com.vinculo.domain.inventory.model.Lot;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class FefoStockService {

    public List<LotAllocation> allocateStock(String productName, BigDecimal quantity, List<Lot> availableLots) {
        List<LotAllocation> allocations = new ArrayList<>();
        BigDecimal remaining = quantity;

        for (Lot lot : availableLots) {
            if (remaining.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }

            if (!lot.hasStock()) {
                continue;
            }

            BigDecimal available = lot.getQuantity();
            BigDecimal toAllocate = remaining.compareTo(available) > 0 ? available : remaining;

            allocations.add(new LotAllocation(lot, toAllocate));
            remaining = remaining.subtract(toAllocate);
        }

        return allocations;
    }

    public BigDecimal getTotalAvailableStock(List<Lot> lots) {
        return lots.stream()
                .filter(Lot::hasStock)
                .map(Lot::getQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public record LotAllocation(Lot lot, BigDecimal quantity) {}
}
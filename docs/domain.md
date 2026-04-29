# Domain Model

## Bounded Contexts

### 1. Donation Context

**Responsibility**: Managing donation offers from donors.

**Core Entity**: `DonationOffer`
- `id`: UUID
- `status`: PENDING | ACCEPTED | REJECTED | REDIRECTED
- `donor`: Donor info
- `items`: List of DonationItem
- `warehouseId`: UUID (after acceptance)

**Core Entity**: `Donor`
- `name`: String
- `contact`: String (email)
- `type`: INDIVIDUAL | COMPANY | NGO

**Flow**:
```
Donor → DonationOffer (PENDING) → Accept → Lot (Inventory)
                                     → Reject → REJECTED
```

### 2. Inventory Context

**Responsibility**: Managing stock in warehouses.

**Core Entity**: `Warehouse`
- `id`: UUID
- `name`: String
- `type`: WAREHOUSE | DISTRIBUTION_CENTER
- `status`: ACTIVE | INACTIVE
- `location`: Address info
- `lots`: List of Lot

**Core Entity**: `Lot`
- `id`: UUID
- `productId`: UUID
- `quantity`: BigDecimal
- `unit`: KG | LITER | UNIT
- `expiryDate`: LocalDate
- `warehouse`: Reference

**Core Entity**: `StockReservation`
- `id`: UUID
- `lot`: Reference
- `quantity`: BigDecimal
- `status`: PENDING | FULFILLED | CANCELLED

### 3. User Context

**Responsibility**: Authentication and authorization.

**Core Entity**: `User`
- `id`: UUID
- `email`: String
- `password`: Hashed
- `userName`: String
- `role`: ADMIN | VOLUNTEER | PARTNER
- `active`: Boolean

**Core Entity**: `Partner`
- `id`: UUID
- `name`: String
- `type`: NGO | GOVERNMENT | COMPANY
- `location`: String

---

## Portos e Adaptadores

### Donation → Inventory Interface

```java
public interface StockManagementPort {
    LotResponse createLotInWarehouse(UUID warehouseId, LotRequest request);
    void allocateStock(UUID warehouseId, UUID productId, int quantity);
}
```

`DonationService` depends only on `StockManagementPort`, not on Inventory internal models.
`LotServiceImpl` implements both `LotService` and `StockManagementPort`.
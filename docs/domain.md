# Domain Model

## Bounded Contexts

### 1. Donation Context

**Responsibility**: Managing donation offers from donors.
**Role in application**: Receives donations from donors (individuals, companies, NGOs) and converts them into stock in warehouses.

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

### 2. Request Context

**Responsibility**: Managing aid requests from beneficiaries.
**Role in application**: Allows beneficiaries to request aid items. When approved, allocates stock from warehouses to fulfill the request.

**Core Entity**: `Request`
- `id`: UUID
- `status`: PENDING | APPROVED | REJECTED | FULFILLED
- `beneficiary`: Beneficiary info
- `items`: List of RequestItem
- `disasterId`: UUID
- `warehouseId`: UUID (after approval)

**Core Entity**: `Beneficiary`
- `name`: String
- `contact`: String
- `documentId`: String

**Flow**:
```
Beneficiary → Request (PENDING) → Approve → Allocate Stock (Inventory)
                                          → Reject → REJECTED
                                          → Fulfill → FULFILLED
```

### 3. Disaster Context

**Responsibility**: Managing disaster events.
**Role in application**: Tracks disaster events (floods, earthquakes, etc.) that generate donation offers and aid requests. Links donations and requests to specific disasters.

**Core Entity**: `Disaster`
- `id`: UUID
- `name`: String (ex: "Enchente SP 2026")
- `type`: FLOOD | EARTHQUAKE | FIRE | PANDEMIC | OTHER
- `status`: ACTIVE | INACTIVE
- `location`: String
- `startDate`: LocalDate
- `endDate`: LocalDate (when deactivated)

**Flow**:
```
Create Disaster → ACTIVE
              ↓
            Deactivate → INACTIVE
              ↓
            Reactivate → ACTIVE
```

### 4. Inventory Context

**Responsibility**: Managing stock in warehouses.
**Role in application**: Central stock management. Receives lots from accepted donations and provides stock for approved requests using FEFO (First Expiry First Out) strategy.

**Core Entity**: `Warehouse`
- `id`: UUID
- `name`: String
- `type`: WAREHOUSE | DISTRIBUTION_CENTER
- `status`: ACTIVE | INACTIVE
- `location`: Address info
- `lots`: List of Lot

**Core Entity**: `Lot`
- `id`: UUID
- `productName`: String
- `quantity`: BigDecimal
- `unit`: KG | LITER | UNIT
- `expiryDate`: LocalDate
- `warehouse`: Reference

**Core Entity**: `StockReservation`
- `id`: UUID
- `lot`: Reference
- `quantity`: BigDecimal
- `status`: PENDING | FULFILLED | CANCELLED

### 5. User Context

**Responsibility**: Authentication and authorization.
**Role in application**: Manages user accounts, authentication (JWT), and role-based access control (ADMIN, VOLUNTEER, PARTNER).

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


## Ports and Adapters

### Donation → Inventory Interface

```java
public interface StockManagementPort {
    LotResponse createLotInWarehouse(UUID warehouseId, LotRequest request);
    void allocateStock(UUID warehouseId, String productName, int quantity);
}
```

`DonationService` depends only on `StockManagementPort`, not on Inventory internal models.
`RequestService` also depends on `StockManagementPort` to allocate stock when approving requests.
`LotServiceImpl` implements both `LotService` and `StockManagementPort`.
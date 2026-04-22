# 🌊 Vínculo — Disaster Management & Donation System

## Context

In disaster scenarios (floods, fires, earthquakes), communities need coordinated support. Warehouses collect donations and distribute them to affected populations — people who lost homes, have special needs, or cannot provide for themselves.

**Core Problem:**
- **Prevent surplus**: disorganized donations lead to waste
- **Fair distribution**: organizations need a reliable way to request and receive stock
- **Traceability**: every donation and distribution is tracked for accountability

---

## Architecture Overview

**Monolithic modular application** with clearly separated bounded contexts. Designed for simplicity in MVP, with bounded contexts prepared for future extraction into microservices if needed.

```
┌───────────────────────────────────────────────────────────────────────────┐
│                              CLIENTS                                      │
│                                                                           │
│   ┌─────────────┐   ┌─────────────┐   ┌─────────────┐   ┌─────────────┐   │
│   │  Browser    │   │  Browser    │   │  Browser    │   │    API      │   │
│   │  Angular    │   │  Angular    │   │  Angular    │   │  (Future)   │   │
│   │  Volunteer  │   │  Warehouse  │   │  Entity     │   │  Mobile     │   │
│   │             │   │  Manager    │   │  Distrib.   │   │  App        │   │
│   └──────┬──────┘   └──────┬──────┘   └──────┬──────┘   └──────┬──────┘   │
└──────────┼─────────────────┼─────────────────┼─────────────────┼──────────┘
           │                 │                 │                 │
           └─────────────────┴────────┬────────┴─────────────────┘
                                      │
                                      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                           REST API (Spring Boot)                            │
│                                                                             │
│   ┌─────────────┐   ┌─────────────┐   ┌─────────────┐   ┌─────────────┐     │
│   │   Auth      │   │  Disaster   │   │  Inventory  │   │  Donation   │     │
│   │ Controller  │   │ Controller  │   │ Controller  │   │ Controller  │     │
│   └──────┬──────┘   └──────┬──────┘   └──────┬──────┘   └──────┬──────┘     │
│          │                 │                 │                 │            │
│          └─────────────────┴──────────┬──────┴─────────────────┘            │
│                                       ▼                                     │
│                          ┌─────────────────────────┐                        │ 
│                          │    Spring Events        │                        │
│                          │  (Internal Messaging)   │                        │
│                          └─────────────────────────┘                        │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Tech Stack

| Layer | Technology |
|-------|------------|
| Language | Java 21 |
| Framework | Spring Boot 3.x |
| Messaging | Spring Events (internal) |
| Persistence | PostgreSQL + JPA/Hibernate |
| API Style | REST (OpenAPI/Swagger) |
| Security | JWT |
| Patterns | DDD, Bounded Contexts, FEFO |

---

## Bounded Contexts

```
┌───────────────────────────────────────────────────────────────────────────────┐
│                             DOMAIN LAYER                                      │
│                                                                               │
│   ┌─────────── ┐  ┌──────────────┐  ┌───────────┐  ┌───────────┐  ┌─────────┐ │
│   │ Disaster   │  │ Inventory    │  │ Donation  │  │ Request   │  │  Users  │ │
│   │            │  │              │  │           │  │           │  │         │ │
│   │ •Event     │  │ •Warehouse   │  │ •Offer    │  │ •Request  │  │ •User   │ │
│   │ •Case      │  │ •Lot         │  │ •Item     │  │ •Item     │  │ •Role   │ │
│   │ •Assessment│  │ •Stock       │  │ •Decision │  │ •Priority │  │ •Partner│ │
│   │ •Need      │  │ •Reservation │  │           │  │           │  │         │ │
│   └────────────┘  └──────────────┘  └───────────┘  └───────────┘  └─────────┘ │
│                                                                               │
│   Future extraction path: each context → independent microservice             │
│                                                                               │
└───────────────────────────────────────────────────────────────────────────────┘
```

### Context Dependencies

```
Donations ──writes──► Inventory ◄──reserves── Requests
    ▲                                            │
    │              Disaster                      │
    └────────────triggers────────────────────────┘
```

---

## Context: Users

> Shared kernel for authentication and authorization.

#### Entity: `User`
| Field | Type | Description |
|-------|------|-------------|
| `id` | UUID | Unique identifier |
| `email` | String | Login email |
| `password` | String | Hashed password |
| `name` | String | Display name |
| `role` | Enum | `VOLUNTEER`, `WAREHOUSE_MANAGER`, `ENTITY_USER`, `ADMIN` |
| `partnerId` | UUID | Organization association (nullable) |
| `active` | Boolean | Account status |

#### Entity: `Partner`
| Field | Type | Description |
|-------|------|-------------|
| `id` | UUID | Unique identifier |
| `name` | String | Organization name |
| `type` | Enum | `NGO`, `CIVIL_PROTECTION`, `MUNICIPALITY`, `SOCIAL_INSTITUTION`, `PARISH_COUNCIL`, `FIREFIGHTERS` |
| `contact` | String | Primary contact |
| `zone` | String | Operating area |

#### Role Permissions
| Role | Access |
|------|--------|
| `VOLUNTEER` | Register donations |
| `WAREHOUSE_MANAGER` | Manage warehouse, view stock |
| `ENTITY_USER` | Submit/manage distribution requests |
| `ADMIN` | Full system access |

---

## Context: Inventory

> Core domain. Manages warehouse stock and enforces FEFO logic.

#### Aggregate Root: `Warehouse`
| Field | Type | Description |
|-------|------|-------------|
| `id` | UUID | Unique identifier |
| `name` | String | Warehouse name |
| `type` | Enum | `HOTSPOT`, `WAREHOUSE`, `RESERVE_AREA` |
| `status` | Enum | `ACTIVE`, `INACTIVE` |
| `location` | Value Object | address, latitude, longitude |

#### Entity: `Lot` *(child of Warehouse)*
| Field | Type | Description |
|-------|------|-------------|
| `id` | UUID | Unique identifier |
| `productId` | UUID | Product reference |
| `lotNumber` | String | External batch identifier |
| `quantity` | BigDecimal | Available quantity |
| `unit` | Enum | `KG`, `UNIT`, `LITER` |
| `expiryDate` | LocalDate | Expiry (FEFO ordering) |
| `receivedAt` | LocalDateTime | Receipt timestamp |

#### Entity: `StockReservation`
| Field | Type | Description |
|-------|------|-------------|
| `id` | UUID | Unique identifier |
| `requestId` | UUID | Distribution request reference |
| `lotId` | UUID | Reserved lot |
| `quantityReserved` | BigDecimal | Reserved amount |
| `expiresAt` | LocalDateTime | Reservation TTL |

#### Business Rules
- **FEFO**: Stock exits by nearest expiry date first
- **Reservation TTL**: Reservations expire if not fulfilled within a configurable period
- **Allocation**: When request is fulfilled, stock is deducted from reserved lots

---

## Context: Donations

> Handles inbound donation offers from individuals, companies, and retail stores.

#### Aggregate Root: `DonationOffer`
| Field | Type | Description |
|-------|------|-------------|
| `id` | UUID | Unique identifier |
| `createdAt` | LocalDateTime | Registration timestamp |
| `status` | Enum | `PENDING`, `ACCEPTED`, `REJECTED`, `REDIRECTED` |
| `warehouseId` | UUID | Target warehouse (if accepted) |
| `donor` | Value Object | name, contact, type |

#### Entity: `DonationItem` *(child of DonationOffer)*
| Field | Type | Description |
|-------|------|-------------|
| `id` | UUID | Unique identifier |
| `productId` | UUID | Product reference |
| `quantity` | BigDecimal | Offered quantity |
| `unit` | Enum | `KG`, `UNIT`, `LITER` |
| `expiryDate` | LocalDate | Batch expiry (optional) |

#### Value Object: `Donor`
| Field | Type | Description |
|-------|------|-------------|
| `name` | String | Donor name (nullable = anonymous) |
| `contact` | String | Phone or email (optional) |
| `type` | Enum | `INDIVIDUAL`, `COMPANY`, `SUPERMARKET` |

#### Decision Flow
```
PENDING → ACCEPTED (creates stock lots in Inventory)
PENDING → REJECTED (donation refused)
PENDING → REDIRECTED (routed to another warehouse/context)
```

---

## Context: Requests

> Handles distribution requests from organizations (NGOs, civil protection, municipalities, social institutions).

#### Aggregate Root: `DistributionRequest`
| Field | Type | Description |
|-------|------|-------------|
| `id` | UUID | Unique identifier |
| `createdAt` | LocalDateTime | Request timestamp |
| `status` | Enum | `PENDING` → `APPROVED` / `REJECTED` → `IN_PREPARATION` → `DELIVERED` |
| `warehouseId` | UUID | Target warehouse |
| `priority` | Enum | `LOW`, `MEDIUM`, `HIGH`, `CRITICAL` |
| `organization` | Value Object | name, contact, zone, type |
| `deliveryAddress` | Value Object | address, notes |

#### Entity: `RequestItem` *(child of DistributionRequest)*
| Field | Type | Description |
|-------|------|-------------|
| `id` | UUID | Unique identifier |
| `productId` | UUID | Requested product |
| `quantityRequested` | BigDecimal | Requested quantity |
| `quantityFulfilled` | BigDecimal | Actually reserved quantity |
| `unit` | Enum | `KG`, `UNIT`, `LITER` |

#### Request Flow
```
PENDING → (stock check) → APPROVED (sufficient stock)
                        → REJECTED (insufficient stock)
APPROVED → IN_PREPARATION → DELIVERED
```

---

## Context: Disaster

> Manages disaster events and assessment of affected population needs.

#### Aggregate Root: `DisasterEvent`
| Field | Type | Description |
|-------|------|-------------|
| `id` | UUID | Unique identifier |
| `type` | Enum | `FLOOD`, `FIRE`, `EARTHQUAKE`, `OTHER` |
| `location` | Value Object | address, latitude, longitude |
| `occurredAt` | LocalDateTime | Event timestamp |
| `status` | Enum | `ACTIVE`, `STABILIZED`, `CLOSED` |

#### Entity: `Case` *(child of DisasterEvent)*
| Field | Type | Description |
|-------|------|-------------|
| `id` | UUID | Unique identifier |
| `affectedArea` | String | Geographic description |
| `severity` | Enum | `LOW`, `MEDIUM`, `HIGH`, `CRITICAL` |
| `status` | Enum | `ASSESSING`, `NEEDS_DEFINED`, `CLOSED` |

#### Entity: `Need` *(child of Case)*
| Field | Type | Description |
|-------|------|-------------|
| `id` | UUID | Unique identifier |
| `productId` | UUID | Needed product |
| `quantityNeeded` | BigDecimal | Required quantity |
| `priority` | Enum | `LOW`, `MEDIUM`, `HIGH`, `CRITICAL` |

---

## REST API Contracts

### Donations — `POST /api/v1/donations`
```json
{
  "donor": {
    "name": "John Doe",
    "contact": "john@example.com",
    "type": "INDIVIDUAL"
  },
  "items": [
    {
      "productId": "uuid",
      "quantity": 50.0,
      "unit": "KG",
      "expiryDate": "2027-01-01"
    }
  ]
}
```
**Response:** `202 Accepted`

---

### Requests — `POST /api/v1/requests`
```json
{
  "warehouseId": "uuid",
  "organization": {
    "name": "Food Bank Porto",
    "contact": "porto@foodbank.pt",
    "zone": "Porto",
    "type": "NGO"
  },
  "deliveryAddress": {
    "address": "123 Porto Street",
    "notes": "Side entrance"
  },
  "priority": "HIGH",
  "items": [
    {
      "productId": "uuid",
      "quantityRequested": 30.0,
      "unit": "KG"
    }
  ]
}
```
**Response:** `202 Accepted` + `{ "requestId": "uuid", "status": "PENDING" }`

### Requests — `GET /api/v1/requests/{id}`
**Response:** Full `DistributionRequest` with current status.

---

## MVP Scope

- [ ] User authentication (JWT)
- [ ] Donation offer registration + approval workflow
- [ ] Stock management with FEFO
- [ ] Distribution request + stock reservation
- [ ] Basic role-based access control
- [ ] Disaster event management
- [ ] Need assessment per disaster case

---

## Implementation Tasks

### Phase 1: Foundation (Users + Security)

- [ ] **1.1** Create `User` entity with fields: id, email, password, name, role, partnerId, active
- [ ] **1.2** Create `Partner` entity with fields: id, name, type, contact, zone
- [ ] **1.3** Create enum `Role`: VOLUNTEER, WAREHOUSE_MANAGER, ENTITY_USER, ADMIN
- [ ] **1.4** Create enum `PartnerType`: NGO, CIVIL_PROTECTION, MUNICIPALITY, SOCIAL_INSTITUTION, PARISH_COUNCIL, FIREFIGHTERS
- [ ] **1.5** Create `UserRepository` interface extending JpaRepository
- [ ] **1.6** Create `PartnerRepository` interface extending JpaRepository
- [ ] **1.7** Implement JWT authentication (filter, provider, service)
- [ ] **1.8** Implement password hashing with BCrypt
- [ ] **1.9** Create login endpoint `POST /api/v1/auth/login`
- [ ] **1.10** Create register endpoint `POST /api/v1/auth/register`
- [ ] **1.11** Implement role-based access control (Spring Security)
- [ ] **1.12** Create OpenAPI configuration

### Phase 2: Inventory (Core Domain)

- [ ] **2.1** Create `Warehouse` aggregate root with fields: id, name, type, status, location
- [ ] **2.2** Create `WarehouseType` enum: HOTSPOT, WAREHOUSE, RESERVE_AREA
- [ ] **2.3** Create `WarehouseStatus` enum: ACTIVE, INACTIVE
- [ ] **2.4** Create `Location` value object: address, latitude, longitude
- [ ] **2.5** Create `Lot` entity (child of Warehouse): id, productId, lotNumber, quantity, unit, expiryDate, receivedAt
- [ ] **2.6** Create `Unit` enum: KG, UNIT, LITER
- [ ] **2.7** Create `StockReservation` entity: id, requestId, lotId, quantityReserved, expiresAt
- [ ] **2.8** Create `WarehouseRepository` interface
- [ ] **2.9** Create `LotRepository` interface with FEFO query
- [ ] **2.10** Create `StockReservationRepository` interface
- [ ] **2.11** Implement FEFO stock exit logic (query lots by expiryDate ASC)
- [ ] **2.12** Implement stock reservation with TTL
- [ ] **2.13** Implement stock deduction on fulfillment
- [ ] **2.14** Create warehouse CRUD endpoints `GET/POST/PUT /api/v1/warehouses`
- [ ] **2.15** Create lot management endpoints `POST /api/v1/warehouses/{id}/lots`
- [ ] **2.16** Create stock view endpoint `GET /api/v1/warehouses/{id}/stock`

### Phase 3: Donations

- [ ] **3.1** Create `DonationOffer` aggregate root: id, createdAt, status, warehouseId, donor
- [ ] **3.2** Create `DonationOfferStatus` enum: PENDING, ACCEPTED, REJECTED, REDIRECTED
- [ ] **3.3** Create `Donor` value object: name, contact, type
- [ ] **3.4** Create `DonorType` enum: INDIVIDUAL, COMPANY, SUPERMARKET
- [ ] **3.5** Create `DonationItem` entity (child of DonationOffer): id, productId, quantity, unit, expiryDate
- [ ] **3.6** Create `Product` entity: id, name, category, description
- [ ] **3.7** Create `ProductCategory` enum: FOOD, WATER, CLOTHING, MEDICINE, HYGIENE, BEDDING, EQUIPMENT, OTHER
- [ ] **3.8** Create `DonationOfferRepository` interface
- [ ] **3.9** Create `DonationItemRepository` interface
- [ ] **3.10** Create `ProductRepository` interface
- [ ] **3.11** Implement donation creation (POST /api/v1/donations)
- [ ] **3.12** Implement donation approval workflow (updates status and creates lots in warehouse)
- [ ] **3.13** Implement donation rejection workflow
- [ ] **3.14** Create endpoint to list pending donations `GET /api/v1/donations?status=PENDING`
- [ ] **3.15** Create endpoint to view donation details `GET /api/v1/donations/{id}`

### Phase 4: Requests

- [ ] **4.1** Create `DistributionRequest` aggregate root: id, createdAt, status, warehouseId, priority, organization, deliveryAddress
- [ ] **4.2** Create `RequestStatus` enum: PENDING, APPROVED, REJECTED, IN_PREPARATION, DELIVERED
- [ ] **4.3** Create `Priority` enum: LOW, MEDIUM, HIGH, CRITICAL
- [ ] **4.4** Create `Organization` value object: name, contact, zone, type
- [ ] **4.5** Create `DeliveryAddress` value object: address, notes
- [ ] **4.6** Create `RequestItem` entity (child of DistributionRequest): id, productId, quantityRequested, quantityFulfilled, unit
- [ ] **4.7** Create `DistributionRequestRepository` interface
- [ ] **4.8** Create `RequestItemRepository` interface
- [ ] **4.9** Implement request creation (POST /api/v1/requests)
- [ ] **4.10** Implement stock availability check
- [ ] **4.11** Implement request approval with stock reservation
- [ ] **4.12** Implement request rejection
- [ ] **4.13** Implement fulfillment flow (IN_PREPARATION → DELIVERED)
- [ ] **4.14** Create endpoint to list requests `GET /api/v1/requests`
- [ ] **4.15** Create endpoint to view request details `GET /api/v1/requests/{id}`
- [ ] **4.16** Create endpoint to update request status `PATCH /api/v1/requests/{id}/status`

### Phase 5: Disaster

- [ ] **5.1** Create `DisasterEvent` aggregate root: id, type, location, occurredAt, status
- [ ] **5.2** Create `DisasterType` enum: FLOOD, FIRE, EARTHQUAKE, OTHER
- [ ] **5.3** Create `DisasterStatus` enum: ACTIVE, STABILIZED, CLOSED
- [ ] **5.4** Create `Case` entity (child of DisasterEvent): id, affectedArea, severity, status
- [ ] **5.5** Create `Severity` enum: LOW, MEDIUM, HIGH, CRITICAL
- [ ] **5.6** Create `CaseStatus` enum: ASSESSING, NEEDS_DEFINED, CLOSED
- [ ] **5.7** Create `Need` entity (child of Case): id, productId, quantityNeeded, priority
- [ ] **5.8** Create `DisasterEventRepository` interface
- [ ] **5.9** Create `CaseRepository` interface
- [ ] **5.10** Create `NeedRepository` interface
- [ ] **5.11** Implement disaster event creation (POST /api/v1/disasters)
- [ ] **5.12** Implement case management (child of disaster)
- [ ] **5.13** Implement need assessment per case
- [ ] **5.14** Create endpoint to list disasters `GET /api/v1/disasters`
- [ ] **5.15** Create endpoint to view disaster details with cases `GET /api/v1/disasters/{id}`
- [ ] **5.16** Create endpoint to add case to disaster `POST /api/v1/disasters/{id}/cases`
- [ ] **5.17** Create endpoint to add need to case `POST /api/v1/disasters/{id}/cases/{caseId}/needs`

### Phase 6: Integration & Polish

- [ ] **6.1** Integrate Donations → Inventory (create lots on donation approval)
- [ ] **6.2** Integrate Requests → Inventory (reserve stock on request approval)
- [ ] **6.3** Integrate Disaster → Needs for request prioritization
- [ ] **6.4** Add input validation on all endpoints
- [ ] **6.5** Add global exception handling
- [ ] **6.6** Add health check endpoint `GET /actuator/health`
- [ ] **6.7** Configure PostgreSQL as production database
- [ ] **6.8** Add basic unit tests for core services
- [ ] **6.9** Add integration tests for API endpoints

---

## Out of Scope (MVP)
- Mobile/web frontend
- Real-time notifications (push/WebSocket)
- Warehouse routing based on geolocation
- Donor profile management
- Reports / analytics dashboard

---

## Project Structure

```
vinculo/
└── src/main/java/com/vinculo/
    ├── api/                      # REST Controllers
    │   ├── auth/
    │   ├── donation/
    │   ├── request/
    │   ├── inventory/
    │   └── disaster/
    ├── application/              # Services, Commands, Queries
    │   ├── donation/
    │   ├── request/
    │   ├── inventory/
    │   └── disaster/
    ├── domain/                   # Bounded Contexts
    │   ├── users/                # Shared kernel
    │   │   ├── entity/
    │   │   └── repository/
    │   ├── donation/
    │   │   ├── model/
    │   │   └── repository/
    │   ├── request/
    │   │   ├── model/
    │   │   └── repository/
    │   ├── inventory/
    │   │   ├── model/
    │   │   └── repository/
    │   └── disaster/
    │       ├── model/
    │       └── repository/
    ├── infrastructure/           # Persistence, Security, Events
    │   ├── persistence/
    │   ├── security/
    │   └── event/
    └── VinculoApplication.java
```

---

## Future Extensibility

The bounded contexts are designed with clear interfaces, making future extraction to microservices straightforward:

| Context | Future Service | Communication |
|---------|---------------|---------------|
| Users | identity-service | REST |
| Inventory | inventory-service | REST + Events |
| Donations | donation-service | REST + Events |
| Requests | request-service | REST + Events |
| Disaster | disaster-service | REST |

Internal Spring Events can be replaced with Apache Kafka when scale demands it.

---

## Portfolio Value

This project demonstrates:

- **Domain-Driven Design**: Bounded contexts, aggregates, entities, value objects
- **Modular Monolith**: Clear separation prepared for future microservices
- **Enterprise Patterns**: FEFO inventory, reservation with TTL, role-based access
- **Modern Java**: Java 21 features, Spring Boot 3.x, records, pattern matching
- **API Design**: REST with OpenAPI/Swagger documentation
- **Security**: JWT authentication and role-based authorization

---

## License

MIT

---

*Vínculo — Connecting those in need with those who can give*

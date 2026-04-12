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
┌─────────────────────────────────────────────────────────────────────────────┐
│                              CLIENTS                                        │
│                                                                             │
│   ┌─────────────┐   ┌─────────────┐   ┌─────────────┐   ┌─────────────┐   │
│   │  Browser    │   │  Browser    │   │  Browser    │   │    API      │   │
│   │  Angular    │   │  Angular    │   │  Angular    │   │  (Future)   │   │
│   │  Volunteer  │   │  Warehouse  │   │  Entity    │   │  Mobile     │   │
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
│   ┌─────────────┐   ┌─────────────┐   ┌─────────────┐   ┌─────────────┐   │
│   │   Auth      │   │  Disaster   │   │  Inventory  │   │  Donation  │   │
│   │ Controller │   │ Controller  │   │ Controller  │   │ Controller │   │
│   └──────┬──────┘   └──────┬──────┘   └──────┬──────┘   └──────┬──────┘   │
│          │                 │                 │                 │           │
│          └─────────────────┴────────┬──────┴─────────────────┘           │
│                                       ▼                                    │
│                          ┌─────────────────────────┐                      │
│                          │    Spring Events         │                      │
│                          │  (Internal Messaging)    │                      │
│                          └─────────────────────────┘                      │
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
┌─────────────────────────────────────────────────────────────────────────────┐
│                             DOMAIN LAYER                                    │
│                                                                             │
│   ┌───────────┐  ┌───────────┐  ┌───────────┐  ┌───────────┐  ┌─────────┐ │
│   │ Disaster  │  │ Inventory │  │ Donation  │  │ Request   │  │  Users  │ │
│   │           │  │           │  │           │  │           │  │         │ │
│   │ •Event    │  │ •Warehouse│  │ •Offer    │  │ •Request  │  │ •User   │ │
│   │ •Case     │  │ •Lot      │  │ •Item     │  │ •Item     │  │ •Role   │ │
│   │ •Assessment│ │ •Stock   │  │ •Decision │  │ •Priority │  │ •Partner│ │
│   │ •Need     │  │ •Reservation│ │           │  │           │  │         │ │
│   └───────────┘  └───────────┘  └───────────┘  └───────────┘  └─────────┘ │
│                                                                             │
│   Future extraction path: each context → independent microservice          │
│                                                                             │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Context Dependencies

```
Donations ──writes──► Inventory ◄──reserves── Requests
    ▲                                           │
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

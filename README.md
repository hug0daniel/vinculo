# Vínculo — Disaster Management System

## Context

In disaster scenarios, communities need coordinated support. Warehouses collect donations and distribute to affected populations.

**Core Problem:**
- Prevent surplus: disorganized donations lead to waste
- Fair distribution: organizations need reliable way to request stock
- Traceability: every donation tracked for accountability

---

## Quick Start

```bash
# Run
mvn spring-boot:run

# Test
mvn test
```

**Default credentials:**
- `admin@test.com` / `admin123`

---

## API Endpoints

| Context | Endpoint | Description |
|---------|----------|--------------|
| User | POST /api/v1/users/register | Register user |
| User | POST /api/v1/users/login | Login |
| Donation | POST /api/v1/donations | Create donation |
| Donation | GET /api/v1/donations | List donations |
| Donation | POST /api/v1/donations/{id}/accept | Accept donation |
| Donation | POST /api/v1/donations/{id}/reject | Reject donation |
| Request | POST /api/v1/requests | Create request |
| Request | GET /api/v1/requests | List requests |
| Request | POST /api/v1/requests/{id}/approve | Approve request |
| Request | POST /api/v1/requests/{id}/reject | Reject request |
| Request | POST /api/v1/requests/{id}/fulfill | Fulfill request |
| Warehouse | POST /api/v1/warehouses | Create warehouse |
| Warehouse | GET /api/v1/warehouses | List warehouses |
| Warehouse | GET /api/v1/warehouses/{id}/stock | Get stock |
| Lot | POST /api/v1/warehouses/{id}/lots | Create lot |
| Lot | GET /api/v1/lots/{id} | Get lot |

---

## Architecture

**Modular monolith** with clean architecture:

- `api/` — Controllers, DTOs
- `application/` — Services, Ports
- `domain/` — Entities, Repositories
- `infrastructure/` — Security, Config

**Bounded Contexts:**
- **Donation**: Donor offers
- **Request**: Aid requests from beneficiaries
- **Inventory**: Warehouse, Lot, Stock
- **User**: Authentication, Partners

---

## Tech Stack

- Java 21 + Spring Boot 4.0.5
- PostgreSQL + JPA/Hibernate
- JWT Authentication

---

See `/docs` for detailed documentation.
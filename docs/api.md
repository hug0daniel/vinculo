# API Documentation

## Base URL

```
http://localhost:8080/api/v1
```

## Authentication

All endpoints (except register/login) require Bearer token:

```
Authorization: Bearer <token>
```

## Endpoints

### User

#### Register
```
POST /users/register
Content-Type: application/json

{
    "email": "user@example.com",
    "password": "password123",
    "userName": "username",
    "role": "VOLUNTEER"
}
```

Response: 201 Created

#### Login
```
POST /users/login
Content-Type: application/json

{
    "email": "user@example.com",
    "password": "password123"
}
```

Response: 200 OK
```json
{
    "token": "eyJ..."
}
```

### Donation

#### Create Donation
```
POST /donations
Content-Type: application/json

{
    "donor": {
        "name": "John Doe",
        "contact": "john@example.com",
        "type": "INDIVIDUAL"
    },
    "items": [
        {
            "productId": "uuid",
            "quantity": 50,
            "unit": "KG",
            "expiryDate": "2025-12-31"
        }
    ]
}
```

Response: 201 Created

#### List Donations
```
GET /donations
GET /donations?status=PENDING
```

Response: 200 OK

#### Accept Donation
```
POST /donations/{id}/accept?warehouseId={warehouseId}
```

Response: 200 OK

#### Reject Donation
```
POST /donations/{id}/reject
```
Response: 200 OK

### Request

#### Create Request
```
POST /requests
Content-Type: application/json

{
    "beneficiary": {
        "name": "John Doe",
        "contact": "john@test.com",
        "documentId": "DOC-001"
    },
    "disasterId": "uuid",
    "items": [
        {
            "productName": "Rice",
            "quantity": 50,
            "unit": "KG"
        }
    ]
}
```
Response: 201 Created

#### List Requests
```
GET /requests
```
Response: 200 OK

#### Get Request
```
GET /requests/{id}
```
Response: 200 OK

#### Approve Request
```
POST /requests/{id}/approve?warehouseId={warehouseId}
```
Response: 200 OK

#### Reject Request
```
POST /requests/{id}/reject
```
Response: 200 OK

#### Fulfill Request
```
POST /requests/{id}/fulfill
```
Response: 200 OK

### Disaster

#### Create Disaster
```
POST /disasters
Content-Type: application/json

{
    "name": "Enchente SP 2026",
    "type": "FLOOD",
    "location": "São Paulo"
}
```
Response: 201 Created

#### List Disasters
```
GET /disasters
```
Response: 200 OK

#### Get Disaster
```
GET /disasters/{id}
```
Response: 200 OK

#### Update Disaster
```
PUT /disasters/{id}
Content-Type: application/json

{
    "name": "Updated Name",
    "type": "EARTHQUAKE",
    "location": "Updated Location"
}
```
Response: 200 OK

#### Deactivate Disaster
```
POST /disasters/{id}/deactivate
```
Response: 200 OK

#### Reactivate Disaster
```
POST /disasters/{id}/reactivate
```
Response: 200 OK

### Warehouse

#### Create Warehouse
```
POST /warehouses
Content-Type: application/json

{
    "name": "Main Warehouse",
    "type": "WAREHOUSE"
}
```

Response: 201 Created

#### List Warehouses
```
GET /warehouses
```

Response: 200 OK

#### Get Warehouse
```
GET /warehouses/{id}
```

Response: 200 OK

#### Get Stock
```
GET /warehouses/{id}/stock
```

Response: 200 OK

#### Activate
```
PUT /warehouses/{id}/activate
```

Response: 200 OK

#### Deactivate
```
DELETE /warehouses/{id}
```

Response: 200 OK

### Lot

#### Create Lot
```
POST /warehouses/{warehouseId}/lots
Content-Type: application/json

{
    "productId": "uuid",
    "quantity": 50,
    "unit": "KG",
    "expiryDate": "2025-12-31"
}
```

Response: 201 Created

#### Get Lot
```
GET /lots/{id}
```

Response: 200 OK

#### List Lots by Warehouse
```
GET /warehouses/{warehouseId}/lots
```

Response: 200 OK

---

## HTTP Status Codes

| Code | Meaning |
|------|---------|
| 200 | OK |
| 201 | Created |
| 400 | Bad Request |
| 401 | Unauthorized |
| 403 | Forbidden |
| 404 | Not Found |
| 409 | Conflict |

---

## Error Response

```json
{
    "error": "Resource not found"
}
```

### Validation Errors

```json
{
    "errors": [
        {
            "field": "name",
            "message": "must not be blank"
        }
    ]
}
```
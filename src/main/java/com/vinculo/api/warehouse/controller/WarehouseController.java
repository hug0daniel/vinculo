package com.vinculo.api.warehouse.controller;

import com.vinculo.api.warehouse.dto.WarehouseRequest;
import com.vinculo.api.warehouse.dto.WarehouseResponse;
import com.vinculo.api.warehouse.dto.StockResponse;
import com.vinculo.application.inventory.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/warehouses")
@Tag(name = "Warehouses", description = "Warehouse management")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @PostMapping
    @Operation(summary = "Create warehouse", description = "Register a new warehouse")
    public ResponseEntity<WarehouseResponse> createWarehouse(@Valid @RequestBody WarehouseRequest request) {
        WarehouseResponse response = warehouseService.createWarehouse(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get warehouse by ID")
    public ResponseEntity<WarehouseResponse> getWarehouse(@PathVariable UUID id) {
        WarehouseResponse response = warehouseService.getWarehouse(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "List all warehouses")
    public ResponseEntity<List<WarehouseResponse>> getAllWarehouses() {
        List<WarehouseResponse> response = warehouseService.getAllWarehouses();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deactivate warehouse", description = "Mark warehouse as inactive")
    public ResponseEntity<WarehouseResponse> deactivateWarehouse(@PathVariable UUID id) {
        WarehouseResponse response = warehouseService.deactivateWarehouse(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/activate")
    @Operation(summary = "Activate warehouse", description = "Mark warehouse as active")
    public ResponseEntity<WarehouseResponse> activateWarehouse(@PathVariable UUID id) {
        WarehouseResponse response = warehouseService.activateWarehouse(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/stock")
    @Operation(summary = "Get warehouse stock")
    public ResponseEntity<StockResponse> getStock(@PathVariable UUID id) {
        StockResponse response = warehouseService.getStock(id);
        return ResponseEntity.ok(response);
    }
}
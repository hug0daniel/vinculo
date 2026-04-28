package com.vinculo.api.warehouse.controller;

import com.vinculo.api.warehouse.dto.LotRequest;
import com.vinculo.api.warehouse.dto.LotResponse;
import com.vinculo.api.warehouse.dto.WarehouseRequest;
import com.vinculo.api.warehouse.dto.WarehouseResponse;
import com.vinculo.application.inventory.LotService;
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
@RequestMapping("/api/v1")
@Tag(name = "Warehouses", description = "Warehouse management")
public class WarehouseController {

    private final WarehouseService warehouseService;
    private final LotService lotService;

    public WarehouseController(WarehouseService warehouseService, LotService lotService) {
        this.warehouseService = warehouseService;
        this.lotService = lotService;
    }

    @PostMapping("/warehouses")
    @Operation(summary = "Create warehouse", description = "Register a new warehouse")
    public ResponseEntity<WarehouseResponse> createWarehouse(@Valid @RequestBody WarehouseRequest request) {
        var response = warehouseService.createWarehouse(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/warehouses/{id}")
    @Operation(summary = "Get warehouse by ID")
    public ResponseEntity<WarehouseResponse> getWarehouse(@PathVariable UUID id) {
        var response = warehouseService.getWarehouse(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/warehouses")
    @Operation(summary = "List all warehouses")
    public ResponseEntity<List<WarehouseResponse>> getAllWarehouses() {
        var response = warehouseService.getAllWarehouses();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/warehouses/{id}/deactivate")
    @Operation(summary = "Deactivate warehouse", description = "Mark warehouse as inactive")
    public ResponseEntity<WarehouseResponse> deactivateWarehouse(@PathVariable UUID id) {
        var response = warehouseService.deactivateWarehouse(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/warehouses/{id}/activate")
    @Operation(summary = "Activate warehouse", description = "Mark warehouse as active")
    public ResponseEntity<WarehouseResponse> activateWarehouse(@PathVariable UUID id) {
        var response = warehouseService.activateWarehouse(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/warehouses/{warehouseId}/lots")
    @Operation(summary = "Add lot to warehouse", description = "Register a new lot in warehouse")
    public ResponseEntity<LotResponse> createLot(@PathVariable UUID warehouseId, @Valid @RequestBody LotRequest request) {
        var response = lotService.createLot(warehouseId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/warehouses/{warehouseId}/lots")
    @Operation(summary = "List lots in warehouse")
    public ResponseEntity<List<LotResponse>> getLotsByWarehouse(@PathVariable UUID warehouseId) {
        var response = lotService.getLotsByWarehouse(warehouseId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/lots/{id}")
    @Operation(summary = "Get lot by ID")
    public ResponseEntity<LotResponse> getLot(@PathVariable UUID id) {
        var response = lotService.getLot(id);
        return ResponseEntity.ok(response);
    }
}
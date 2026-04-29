package com.vinculo.api.lot.controller;

import com.vinculo.api.warehouse.dto.LotRequest;
import com.vinculo.api.warehouse.dto.LotResponse;
import com.vinculo.application.inventory.LotService;
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
@Tag(name = "Lots", description = "Lot management")
public class LotController {

    private final LotService lotService;

    public LotController(LotService lotService) {
        this.lotService = lotService;
    }

    @PostMapping("/warehouses/{warehouseId}/lots")
    @Operation(summary = "Create lot", description = "Register a new lot in warehouse")
    public ResponseEntity<LotResponse> createLot(@PathVariable UUID warehouseId, @Valid @RequestBody LotRequest request) {
        LotResponse response = lotService.createLot(warehouseId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/warehouses/{warehouseId}/lots")
    @Operation(summary = "List lots", description = "List all lots in a warehouse")
    public ResponseEntity<List<LotResponse>> getLotsByWarehouse(@PathVariable UUID warehouseId) {
        List<LotResponse> response = lotService.getLotsByWarehouse(warehouseId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/lots/{id}")
    @Operation(summary = "Get lot", description = "Get lot by ID")
    public ResponseEntity<LotResponse> getLot(@PathVariable UUID id) {
        LotResponse response = lotService.getLot(id);
        return ResponseEntity.ok(response);
    }
}
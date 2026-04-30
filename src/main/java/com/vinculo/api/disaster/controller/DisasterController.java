package com.vinculo.api.disaster.controller;

import com.vinculo.api.disaster.dto.CreateDisasterRequest;
import com.vinculo.api.disaster.dto.DisasterItem;
import com.vinculo.api.disaster.dto.DisasterResponse;
import com.vinculo.api.disaster.dto.UpdateDisasterRequest;
import com.vinculo.application.disaster.DisasterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/disasters")
@Tag(name = "Disasters", description = "Disaster event management")
public class DisasterController {

    private final DisasterService disasterService;

    public DisasterController(DisasterService disasterService) {
        this.disasterService = disasterService;
    }

    @PostMapping
    @Operation(summary = "Create disaster", description = "Register a new disaster event")
    public ResponseEntity<DisasterResponse> createDisaster(@Valid @RequestBody CreateDisasterRequest request) {
        DisasterResponse response = disasterService.createDisaster(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update disaster", description = "Update disaster details")
    public ResponseEntity<DisasterResponse> updateDisaster(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateDisasterRequest request) {
        DisasterResponse response = disasterService.updateDisaster(id, request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/deactivate")
    @Operation(summary = "Deactivate disaster", description = "Mark disaster as inactive")
    public ResponseEntity<DisasterResponse> deactivateDisaster(@PathVariable UUID id) {
        DisasterResponse response = disasterService.deactivateDisaster(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/reactivate")
    @Operation(summary = "Reactivate disaster", description = "Reactivate an inactive disaster")
    public ResponseEntity<DisasterResponse> reactivateDisaster(@PathVariable UUID id) {
        DisasterResponse response = disasterService.reactivateDisaster(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get disaster by ID")
    public ResponseEntity<DisasterResponse> getDisaster(@PathVariable UUID id) {
        DisasterResponse response = disasterService.getDisaster(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "List disasters")
    public ResponseEntity<List<DisasterItem>> getDisasters() {
        List<DisasterItem> response = disasterService.getDisasters();
        return ResponseEntity.ok(response);
    }
}

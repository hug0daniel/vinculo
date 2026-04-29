package com.vinculo.api.donation.controller;

import com.vinculo.api.donation.dto.DonationRequest;
import com.vinculo.api.donation.dto.DonationResponse;
import com.vinculo.application.donation.DonationService;
import com.vinculo.domain.donation.model.DonationStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/donations")
@Tag(name = "Donations", description = "Donation offer management")
public class DonationController {

    private final DonationService donationService;

    public DonationController(DonationService donationService) {
        this.donationService = donationService;
    }

    @PostMapping
    @Operation(summary = "Create donation", description = "Register a new donation offer")
    public ResponseEntity<DonationResponse> createDonation(@Valid @RequestBody DonationRequest request) {
        DonationResponse response = donationService.createDonation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get donation by ID")
    public ResponseEntity<DonationResponse> getDonation(@PathVariable UUID id) {
        DonationResponse response = donationService.getDonation(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "List donations", description = "Filter by status (optional)")
    public ResponseEntity<List<DonationResponse>> getDonations(
            @RequestParam(required = false) DonationStatus status) {
        List<DonationResponse> response = donationService.getDonations(status);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/accept")
    @Operation(summary = "Accept donation", description = "Approve a donation and create stock")
    public ResponseEntity<DonationResponse> acceptDonation(@PathVariable UUID id, @RequestParam UUID warehouseId) {
        DonationResponse response = donationService.acceptDonation(id, warehouseId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/reject")
    @Operation(summary = "Reject donation", description = "Reject a donation offer")
    public ResponseEntity<DonationResponse> rejectDonation(@PathVariable UUID id) {
        DonationResponse response = donationService.rejectDonation(id);
        return ResponseEntity.ok(response);
    }
}
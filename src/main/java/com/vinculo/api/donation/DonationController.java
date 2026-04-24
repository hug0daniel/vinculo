package com.vinculo.api.donation;

import com.vinculo.application.donation.DonationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        var response = donationService.createDonation(request);
        return ResponseEntity.accepted().body(response);
    }

    @PostMapping("/{id}/accept")
    @Operation(summary = "Accept donation", description = "Approve a donation and create stock")
    public ResponseEntity<DonationResponse> acceptDonation(@PathVariable UUID id, @RequestParam UUID warehouseId) {
        var response = donationService.acceptDonation(id, warehouseId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/reject")
    @Operation(summary = "Reject donation", description = "Reject a donation offer")
    public ResponseEntity<DonationResponse> rejectDonation(@PathVariable UUID id) {
        var response = donationService.rejectDonation(id);
        return ResponseEntity.ok(response);
    }
}
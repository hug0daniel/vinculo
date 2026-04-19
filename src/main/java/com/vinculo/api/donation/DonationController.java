package com.vinculo.api.donation;

import com.vinculo.application.donation.CreateDonationCommand;
import com.vinculo.application.donation.DonationService;
import com.vinculo.domain.donation.model.Donor;
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
    @Operation(summary = "Create a new donation offer", description = "Registers a new donation offer. Status starts as PENDING.")
    public ResponseEntity<CreateDonationResponse> createDonation(
            @Valid @RequestBody CreateDonationRequest request
    ) {
        Donor donor = new Donor(
            request.donor().name(),
            request.donor().contact(),
            request.donor().type()
        );

        var items = request.items().stream()
            .map(item -> new CreateDonationCommand.Item(
                item.productId(),
                item.quantity(),
                item.unit(),
                item.expiryDate()
            ))
            .toList();

        var command = new CreateDonationCommand(donor, items);
        var response = donationService.createDonation(command);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
    }

    @PostMapping("/{id}/accept")
    @Operation(summary = "Accept a donation", description = "Accepts a donation and assigns it to a warehouse. Creates lots in inventory.")
    public ResponseEntity<CreateDonationResponse> acceptDonation(
            @PathVariable UUID id,
            @RequestParam UUID warehouseId
    ) {
        var response = donationService.acceptDonation(id, warehouseId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/reject")
    @Operation(summary = "Reject a donation", description = "Rejects a donation offer.")
    public ResponseEntity<CreateDonationResponse> rejectDonation(@PathVariable UUID id) {
        var response = donationService.rejectDonation(id);
        return ResponseEntity.ok(response);
    }
}
package com.vinculo.api.request.controller;

import com.vinculo.api.request.dto.RequestDto;
import com.vinculo.application.request.RequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/requests")
@Tag(name = "Requests", description = "Aid request management")
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @PostMapping
    @Operation(summary = "Create request", description = "Register a new aid request")
    public ResponseEntity<RequestDto.RequestResponse> createRequest(@Valid @RequestBody RequestDto.CreateRequestRequest request) {
        RequestDto.RequestResponse response = requestService.createRequest(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get request by ID")
    public ResponseEntity<RequestDto.RequestResponse> getRequest(@PathVariable UUID id) {
        RequestDto.RequestResponse response = requestService.getRequest(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "List requests")
    public ResponseEntity<List<RequestDto.RequestListItem>> getRequests() {
        List<RequestDto.RequestListItem> response = requestService.getRequests();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/approve")
    @Operation(summary = "Approve request", description = "Approve a pending request and allocate stock")
    public ResponseEntity<RequestDto.RequestResponse> approveRequest(
            @PathVariable UUID id,
            @RequestParam UUID warehouseId) {
        RequestDto.RequestResponse response = requestService.approveRequest(id, warehouseId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/reject")
    @Operation(summary = "Reject request", description = "Reject a pending request")
    public ResponseEntity<RequestDto.RequestResponse> rejectRequest(@PathVariable UUID id) {
        RequestDto.RequestResponse response = requestService.rejectRequest(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/fulfill")
    @Operation(summary = "Fulfill request", description = "Mark an approved request as fulfilled")
    public ResponseEntity<RequestDto.RequestResponse> fulfillRequest(@PathVariable UUID id) {
        RequestDto.RequestResponse response = requestService.fulfillRequest(id);
        return ResponseEntity.ok(response);
    }
}

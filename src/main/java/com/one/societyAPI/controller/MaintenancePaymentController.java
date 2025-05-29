package com.one.societyAPI.controller;

import com.one.societyAPI.dto.MaintenancePaymentDTO;
import com.one.societyAPI.response.StandardResponse;
import com.one.societyAPI.service.MaintenancePaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/maintenance/payments")
@Tag(name = "Maintenance Payments Management", description = "APIs for managing a Maintenance Payments")
public class MaintenancePaymentController {

    private final MaintenancePaymentService paymentService;

    public MaintenancePaymentController(MaintenancePaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/{maintenanceId}")
    @Operation(summary = "Get All Maintenance Payments by maintenanceId", description = "Get Maintenance Payments by maintenanceId")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'USER')")
    public ResponseEntity<StandardResponse<List<MaintenancePaymentDTO>>> getPayments(@PathVariable Long maintenanceId) {
        List<MaintenancePaymentDTO> result = paymentService.getPaymentsByMaintenance(maintenanceId);
        return ResponseEntity.ok(StandardResponse.success("Payments fetched successfully", result));
    }

    @PutMapping("/update-status")
    @Operation(summary = "Super Admin and Admin Can Update Maintenance Status (PAID/UNPAID)", description = "Super Admin and Admin Can Update Maintenance Status (PAID/UNPAID)")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<StandardResponse<MaintenancePaymentDTO>> updateStatus(
            @RequestParam Long maintenanceId,
            @RequestParam Long userId,
            @RequestParam String status // "PAID" or "PENDING"
    ) {
        MaintenancePaymentDTO updated = paymentService.updatePaymentStatus(maintenanceId, userId, status);
        return ResponseEntity.ok(StandardResponse.success("Payment status updated successfully", updated));
    }

    @GetMapping("/{maintenanceId}/status")
    @Operation(summary = "Get Maintenance By Status (PAID/PENDING)", description = "Filter by Status, Month, and Year")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'USER')")
    public ResponseEntity<StandardResponse<List<MaintenancePaymentDTO>>> getPaymentsByStatus(
            @PathVariable Long maintenanceId,
            @RequestParam String status,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year
    ) {
        List<MaintenancePaymentDTO> list = paymentService.getPaymentsByStatus(maintenanceId, status, month, year);
        return ResponseEntity.ok(StandardResponse.success("Filtered payments fetched successfully", list));
    }
}
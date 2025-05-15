package com.one.societyAPI.controller;

import com.one.societyAPI.dto.MaintenancePaymentDTO;
import com.one.societyAPI.service.MaintenancePaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    @Operation(summary = "Get Maintenance Payments by maintenanceId", description = "Get Maintenance Payments by maintenanceId")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'USER')")
    public List<MaintenancePaymentDTO> getPayments(@PathVariable Long maintenanceId) {
        return paymentService.getPaymentsByMaintenance(maintenanceId);
    }

    @PutMapping("/update-status")
    @Operation(summary = "Super Admin and Admin Can Update Maintenance Status (PAID/UNPAID)", description = "Super Admin and Admin Can Update Maintenance Status (PAID/UNPAID)")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public MaintenancePaymentDTO updateStatus(
            @RequestParam Long maintenanceId,
            @RequestParam Long userId,
            @RequestParam String status // "PAID" or "PENDING"
    ) {
        return paymentService.updatePaymentStatus(maintenanceId, userId, status);
    }

    @GetMapping("/{maintenanceId}/status")
    @Operation(summary = "Get Maintenance By Status (PAID/UNPAID)", description = "Using Maintenance ID and Maintenance Status (PAID/UNPAID) You Can Get Maintenance Details")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'USER')")
    public List<MaintenancePaymentDTO> getPaymentsByStatus(
            @PathVariable Long maintenanceId,
            @RequestParam String status
    ) {
        return paymentService.getPaymentsByStatus(maintenanceId, status);
    }

}
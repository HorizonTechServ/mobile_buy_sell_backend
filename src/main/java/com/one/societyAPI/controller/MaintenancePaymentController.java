package com.one.societyAPI.controller;

import com.one.societyAPI.dto.MaintenancePaymentDTO;
import com.one.societyAPI.logger.DefaultLogger;
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

    private static final String CLASSNAME = "MaintenancePaymentController";
    private static final DefaultLogger LOGGER = new DefaultLogger(MaintenancePaymentController.class);

    private final MaintenancePaymentService paymentService;

    public MaintenancePaymentController(MaintenancePaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/{maintenanceId}")
    @Operation(summary = "Get All Maintenance Payments by maintenanceId", description = "Get Maintenance Payments by maintenanceId")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'USER')")
    public ResponseEntity<StandardResponse<List<MaintenancePaymentDTO>>> getPayments(@PathVariable Long maintenanceId) {
        String method = "getPayments";
        LOGGER.infoLog(CLASSNAME, method, "Fetching payments for maintenance ID " + maintenanceId);

        List<MaintenancePaymentDTO> result = paymentService.getPaymentsByMaintenance(maintenanceId);

        LOGGER.infoLog(CLASSNAME, method, "Fetched " + result.size() + " payment records", 200L);
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
        String method = "updateStatus";
        LOGGER.infoLog(CLASSNAME, method, "Updating status to " + status + " for maintenance ID " + maintenanceId + " and user ID " + userId);

        try {
            MaintenancePaymentDTO updated = paymentService.updatePaymentStatus(maintenanceId, userId, status);
            LOGGER.infoLog(CLASSNAME, method, "Payment status updated successfully", 200L);
            return ResponseEntity.ok(StandardResponse.success("Payment status updated successfully", updated));
        } catch (Exception ex) {
            LOGGER.infoLog(CLASSNAME, method, "Error while updating payment status", ex);
            throw ex;
        }
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
        String method = "getPaymentsByStatus";
        LOGGER.infoLog(CLASSNAME, method, "Fetching payments for maintenance ID " + maintenanceId +
                " with status " + status +
                (month != null ? ", month " + month : "") +
                (year != null ? ", year " + year : ""));

        List<MaintenancePaymentDTO> list = paymentService.getPaymentsByStatus(maintenanceId, status, month, year);

        LOGGER.infoLog(CLASSNAME, method, "Fetched " + list.size() + " filtered payment records", 200L);
        return ResponseEntity.ok(StandardResponse.success("Filtered payments fetched successfully", list));
    }
}
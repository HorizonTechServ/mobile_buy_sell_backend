package com.one.societyAPI.controller;

import com.one.societyAPI.dto.MaintenanceDTO;
import com.one.societyAPI.dto.UpdateStatusRequest;
import com.one.societyAPI.response.StandardResponse;
import com.one.societyAPI.service.MaintenanceService;
import com.one.societyAPI.utils.PaymentStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/maintenance")
@Tag(name = "Maintenance Management", description = "APIs for managing a maintenance")
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    public MaintenanceController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }

    @PostMapping("/create")
    @Operation(
            summary = "Create a maintenance (ADMIN and SUPER ADMIN Can Create Maintenance)",
            description = "Add new maintenance"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<StandardResponse<MaintenanceDTO>> create(@Valid @RequestBody MaintenanceDTO dto) {
        MaintenanceDTO created = maintenanceService.createMaintenance(dto);
        return ResponseEntity.ok(StandardResponse.success("Maintenance created successfully", created));
    }

    @GetMapping("/society/{societyId}")
    @Operation(
            summary = "Get all maintenance by society (ADMIN and SUPER ADMIN Can GET Maintenance)",
            description = "Get all maintenance by society"
    )
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'USER')")
    public ResponseEntity<StandardResponse<List<MaintenanceDTO>>> getBySociety(@PathVariable Long societyId) {
        List<MaintenanceDTO> list = maintenanceService.getMaintenanceBySociety(societyId);
        return ResponseEntity.ok(StandardResponse.success("Maintenance records fetched successfully", list));
    }

    @GetMapping("/status")
    @Operation(summary = "Get maintenance by status", description = "Get all maintenance with given status for a society")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'USER')")
    public ResponseEntity<StandardResponse<List<MaintenanceDTO>>> getByStatus(
            @RequestParam Long societyId,
            @RequestParam PaymentStatus status) {
        List<MaintenanceDTO> list = maintenanceService.getMaintenanceByStatus(societyId, status);
        return ResponseEntity.ok(StandardResponse.success("Maintenance records fetched by status", list));
    }

    @PutMapping("/update-status")
    @Operation(summary = "Update status of all maintenance payments for a maintenance entry",
            description = "Admin can update status (PAID or PENDING) for all users under a specific maintenance")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<StandardResponse<String>> updateStatusForMaintenance(
            @RequestBody UpdateStatusRequest request) {
        maintenanceService.updateStatusForMaintenance(request.getSocietyId(), request.getMaintenanceId(), request.getStatus());
        return ResponseEntity.ok(StandardResponse.success("Status updated successfully", null));
    }


}
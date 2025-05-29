package com.one.societyAPI.controller;

import com.one.societyAPI.dto.MaintenanceDTO;
import com.one.societyAPI.response.StandardResponse;
import com.one.societyAPI.service.MaintenanceService;
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
}
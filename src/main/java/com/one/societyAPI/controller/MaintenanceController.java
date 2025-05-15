package com.one.societyAPI.controller;

import com.one.societyAPI.dto.MaintenanceDTO;
import com.one.societyAPI.service.MaintenanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    @Operation(summary = "Create a maintenance", description = "add new maintenance")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public MaintenanceDTO create(@RequestBody MaintenanceDTO dto) {
        return maintenanceService.createMaintenance(dto);
    }

    @GetMapping("/society/{societyId}")
    @Operation(summary = "Get all maintenance by society", description = "Get all maintenance by society")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'USER')")
    public List<MaintenanceDTO> getBySociety(@PathVariable Long societyId) {
        return maintenanceService.getMaintenanceBySociety(societyId);
    }
}
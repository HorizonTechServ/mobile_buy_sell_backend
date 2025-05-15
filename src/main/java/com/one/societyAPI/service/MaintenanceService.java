package com.one.societyAPI.service;

import com.one.societyAPI.dto.MaintenanceDTO;

import java.util.List;

public interface MaintenanceService {
    MaintenanceDTO createMaintenance(MaintenanceDTO dto);
    List<MaintenanceDTO> getMaintenanceBySociety(Long societyId);
}
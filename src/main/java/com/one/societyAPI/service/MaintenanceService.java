package com.one.societyAPI.service;

import com.one.societyAPI.dto.MaintenanceDTO;
import com.one.societyAPI.utils.PaymentStatus;

import java.util.List;

public interface MaintenanceService {
    MaintenanceDTO createMaintenance(MaintenanceDTO dto);
    List<MaintenanceDTO> getMaintenanceBySociety(Long societyId);
    List<MaintenanceDTO> getMaintenanceByStatus(Long societyId, PaymentStatus status);
    void updateStatusForMaintenance(Long societyId, Long maintenanceId, PaymentStatus status);
}
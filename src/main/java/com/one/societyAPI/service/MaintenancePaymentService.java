package com.one.societyAPI.service;

import com.one.societyAPI.dto.MaintenancePaymentDTO;

import java.util.List;

public interface MaintenancePaymentService {
    List<MaintenancePaymentDTO> getPaymentsByMaintenance(Long maintenanceId);
    MaintenancePaymentDTO updatePaymentStatus(Long maintenanceId, Long userId, String status); // status = "PAID" or "PENDING"
    public List<MaintenancePaymentDTO> getPaymentsByStatus(Long maintenanceId, String status, Integer month, Integer year);
    List<MaintenancePaymentDTO> getPendingPaymentsByUserId(Long userId, Integer month, Integer year);
}
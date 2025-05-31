package com.one.societyAPI.dto;

import com.one.societyAPI.utils.PaymentStatus;

public class UpdateStatusRequest {
    private Long societyId;
    private Long maintenanceId;
    private PaymentStatus status;

    // Getters and setters
    public Long getSocietyId() { return societyId; }
    public void setSocietyId(Long societyId) { this.societyId = societyId; }

    public Long getMaintenanceId() { return maintenanceId; }
    public void setMaintenanceId(Long maintenanceId) { this.maintenanceId = maintenanceId; }

    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }
}
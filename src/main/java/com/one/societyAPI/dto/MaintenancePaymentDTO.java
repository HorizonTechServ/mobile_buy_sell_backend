package com.one.societyAPI.dto;

import com.one.societyAPI.utils.PaymentStatus;

import java.time.LocalDate;

public record MaintenancePaymentDTO(
        Long id,
        Long maintenanceId,
        Long userId,
        PaymentStatus status,
        LocalDate paymentDate
) {}
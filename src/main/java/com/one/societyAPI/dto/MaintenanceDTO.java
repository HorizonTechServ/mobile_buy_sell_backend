package com.one.societyAPI.dto;

import com.one.societyAPI.utils.PaymentStatus;

import java.time.LocalDate;

public record MaintenanceDTO(
        Long id,
        String description,
        Double amount,
        LocalDate dueDate,
        Long societyId,
        PaymentStatus status
) {}
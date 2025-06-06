package com.one.societyAPI.dto;

import com.one.societyAPI.utils.ComplaintStatus;
import java.time.LocalDateTime;

public record ComplaintDTO(
        Long id,
        Long complaintById,
        String complaintByName,
        String flatNumber,
        Long resolvedById,
        String resolvedByName,
        ComplaintStatus status,
        String description,
        LocalDateTime complaintDate,
        LocalDateTime resolvedDate,
        String complaintImage,       // Base64 string
        Long complaintImageId        // Optional, useful if needed
) {}
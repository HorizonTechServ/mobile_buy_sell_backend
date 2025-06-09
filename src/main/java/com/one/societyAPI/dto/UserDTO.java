package com.one.societyAPI.dto;

import com.one.societyAPI.utils.UserRole;
import com.one.societyAPI.utils.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String mobileNumber;
    private String gender;
    private UserRole role;
    private UserStatus status;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
    private String maintenanceStatus; // "PAID" or "PENDING"
    private FlatRequest flat; // <- new field,
    private Double maintenanceAmount; // <-- Include this in UserDTO
}
package com.one.arpitInstituteAPI.dto;

import com.one.arpitInstituteAPI.utils.UserRole;
import com.one.arpitInstituteAPI.utils.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String mobileNumber;
    private String username;
    private String gender;
    private UserRole role;
    private UserStatus status;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
}
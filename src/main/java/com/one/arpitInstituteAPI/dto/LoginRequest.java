package com.one.arpitInstituteAPI.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Contact Number ID is required")
    private String mobileNumber;

    @NotBlank(message = "Password is required")
    private String password;
}


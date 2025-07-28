package com.one.mobilebuysellAPI.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class BuyingDto {

    private Long id;

    @NotNull(message = "Purchase date is required")
    private LocalDate purchaseDate;

    @NotBlank(message = "IMEI number is required")
    @Size(min = 10, max = 20, message = "IMEI number must be between 10 and 20 characters")
    private String imeiNumber;

    @NotBlank(message = "Model number is required")
    private String modelNumber;

    @NotBlank(message = "Storage is required")
    private String storage;

    @NotBlank(message = "Color is required")
    private String color;

    @NotBlank(message = "Purchase from is required")
    private String purchaseFrom;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "\\d{10}", message = "Mobile number must be 10 digits")
    private String mobileNumber;

    @NotNull(message = "Purchase cost is required")
    @Positive(message = "Purchase cost must be a positive number")
    private Double purchaseCost;

    private String repairingDetails;

    private String soldStatus;
}
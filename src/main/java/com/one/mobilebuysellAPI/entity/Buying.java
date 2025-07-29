package com.one.mobilebuysellAPI.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Buying {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Purchase date is required")
    private LocalDate purchaseDate;

    @NotBlank(message = "IMEI number is required")
    @Size(min = 10, max = 20, message = "IMEI number must be between 10 and 20 characters")
    @Column(unique = true)
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
    @Positive(message = "Purchase cost must be positive")
    private Double purchaseCost;

    private String repairingDetails;

    private String soldStatus = "UNSOLD";
}
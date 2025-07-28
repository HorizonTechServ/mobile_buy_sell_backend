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
public class Selling {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Sell date is required")
    private LocalDate sellDate;

    @NotNull(message = "Sell price is required")
    @Positive(message = "Sell price must be positive")
    private Double sellPrice;

    @NotBlank(message = "Customer name is required")
    @Size(min = 2, max = 100, message = "Customer name must be between 2 and 100 characters")
    private String customerName;

    @NotBlank(message = "Customer contact number is required")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid mobile number")
    private String customerContactNumber;

    @Column(unique = true)
    private String invoiceNumber;

    @NotBlank(message = "Color is required")
    private String color;

    @NotBlank(message = "Model number is required")
    private String modelNumber;

    @NotBlank(message = "Storage is required")
    private String storage;

    @NotBlank(message = "Payment type is required")
    private String paymentType;

    private String sellPriceInWord;

    @ManyToOne
    @JoinColumn(name = "buying_id", referencedColumnName = "id")
    private Buying buying;
}
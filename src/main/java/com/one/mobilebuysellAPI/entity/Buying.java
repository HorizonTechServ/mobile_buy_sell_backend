package com.one.mobilebuysellAPI.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Buying {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate purchaseDate;
    private String imeiNumber;
    private String modelNumber;
    private String storage;
    private String color;
    private String purchaseFrom;
    private String mobileNumber;
    private BigDecimal purchaseCost;
    private String repairing;
}


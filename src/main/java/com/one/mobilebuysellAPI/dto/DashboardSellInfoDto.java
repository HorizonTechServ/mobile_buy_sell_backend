package com.one.mobilebuysellAPI.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSellInfoDto {
    private String modelNumber;
    private String storage;
    private String color;
    private Double purchaseCost;
    private LocalDate sellDate;
    private Double sellPrice;
    private String customerName;
    private Double profit;
}
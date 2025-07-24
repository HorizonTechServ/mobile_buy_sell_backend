package com.one.mobilebuysellAPI.entity;

import jakarta.persistence.*;
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

    private LocalDate sellDate;
    private Double sellPrice;
    private String customerName;
    private String customerContactNumber;
    private String invoiceNumber;

    @ManyToOne
    @JoinColumn(name = "buying_id", referencedColumnName = "id")
    private Buying buying;
}
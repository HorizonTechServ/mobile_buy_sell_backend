package com.one.arpitInstituteAPI.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceiptConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double tuitionFees;
    private Double studentDevFees;
    private Double labLibSportsFees;
    private Double otherFees;

    private String receiptPrefix;
}
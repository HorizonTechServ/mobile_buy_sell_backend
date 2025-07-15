package com.one.arpitInstituteAPI.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String receiptNo;

    private String year;
    private LocalDate date;

    private String studentName;
    private String department;
    private String branch;
    private String semester;

    private Double tuitionFees;
    private Double studentDevFees;
    private Double labLibSportsFees;
    private Double otherFees;

    private Double totalAmount;
    private String amountInWords;

    private String paymentMode; // CASH or CHEQUE
    private String chequeNo;
    private LocalDate chequeDate;

    private String status; // PAID or UNPAID

    private String createdBy;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
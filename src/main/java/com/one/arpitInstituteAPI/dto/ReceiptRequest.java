package com.one.arpitInstituteAPI.dto;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptRequest {
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

    private String paymentMode;
    private String chequeNo;
    private LocalDate chequeDate;
}
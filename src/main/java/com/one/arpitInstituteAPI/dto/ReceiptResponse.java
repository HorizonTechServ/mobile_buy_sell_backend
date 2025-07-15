package com.one.arpitInstituteAPI.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptResponse {
    private String receiptNo;
    private String studentName;
    private String department;
    private String branch;
    private String semester;
    private Double totalAmount;
    private String amountInWords;
    private String paymentMode;
}
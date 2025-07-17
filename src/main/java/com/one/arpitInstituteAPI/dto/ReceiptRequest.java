package com.one.arpitInstituteAPI.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
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

    @NotBlank(message = "Student name is required")
    private String studentName;

    @NotBlank(message = "Student mobile number is required")
    @Pattern(regexp = "\\d{10}", message = "Student mobile number must be exactly 10 digits")
    private String studentMobileNumber;

    @NotBlank(message = "Student email is required")
    @Email(message = "Invalid email format")
    private String studentEmail;

    @NotBlank(message = "Department is required")
    private String department;

    @NotBlank(message = "Branch is required")
    private String branch;

    @NotBlank(message = "Semester is required")
    private String semester;

    @PositiveOrZero(message = "Tuition fees must be zero or positive")
    private Double tuitionFees;

    @PositiveOrZero(message = "Student development fees must be zero or positive")
    private Double studentDevFees;

    @PositiveOrZero(message = "Lab/Library/Sports fees must be zero or positive")
    private Double labLibSportsFees;

    @PositiveOrZero(message = "Other fees must be zero or positive")
    private Double otherFees;

    @NotBlank(message = "Payment mode is required")
    @Pattern(regexp = "CASH|CHEQUE", flags = Pattern.Flag.CASE_INSENSITIVE, message = "Payment mode must be 'CASH' or 'CHEQUE'")
    private String paymentMode;

    private String chequeNo;
    private LocalDate chequeDate;
}
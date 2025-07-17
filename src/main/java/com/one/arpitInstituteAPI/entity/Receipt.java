package com.one.arpitInstituteAPI.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @NotBlank(message = "Year is required")
    private String year;

    @NotNull(message = "Date is required")
    private LocalDate date;

    @NotBlank(message = "Student name is required")
    private String studentName;

    @NotBlank(message = "Mobile number is required")
    @Pattern(regexp = "\\d{10}", message = "Mobile number must be 10 digits")
    private String studentMobileNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String studentEmail;

    @NotBlank(message = "Department is required")
    private String department;

    @NotBlank(message = "Branch is required")
    private String branch;

    @NotBlank(message = "Semester is required")
    private String semester;

    @NotNull(message = "Tuition fees is required")
    @PositiveOrZero(message = "Tuition fees cannot be negative")
    private Double tuitionFees;

    @NotNull(message = "Student Development Fees is required")
    @PositiveOrZero(message = "Student Development Fees cannot be negative")
    private Double studentDevFees;

    @NotNull(message = "Lab/Library/Sports Fees is required")
    @PositiveOrZero(message = "Lab/Library/Sports Fees cannot be negative")
    private Double labLibSportsFees;

    @NotNull(message = "Other Fees is required")
    @PositiveOrZero(message = "Other Fees cannot be negative")
    private Double otherFees;

    @NotNull(message = "Total amount is required")
    @PositiveOrZero(message = "Total amount must be greater than zero")
    private Double totalAmount;

    @NotBlank(message = "Amount in words is required")
    private String amountInWords;

    @NotBlank(message = "Payment mode is required")
    @Pattern(regexp = "CASH|CHEQUE", flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Payment mode must be either 'CASH' or 'CHEQUE'")
    private String paymentMode;

    private String chequeNo;

    private LocalDate chequeDate;

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "PAID|UNPAID", flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Status must be either 'PAID' or 'UNPAID'")
    private String status;

    private String createdBy;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
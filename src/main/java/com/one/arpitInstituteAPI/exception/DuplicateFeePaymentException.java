package com.one.arpitInstituteAPI.exception;

public class DuplicateFeePaymentException extends RuntimeException {
    public DuplicateFeePaymentException(String message) {
        super(message);
    }
}
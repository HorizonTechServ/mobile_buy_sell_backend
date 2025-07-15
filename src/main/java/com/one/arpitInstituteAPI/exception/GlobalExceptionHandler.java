package com.one.arpitInstituteAPI.exception;

import com.one.arpitInstituteAPI.response.StandardResponse;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import java.util.stream.Collectors;

@ControllerAdvice
@Hidden
public class GlobalExceptionHandler {

    // Field-level validation: @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Invalid input");

        return ResponseEntity.badRequest().body(StandardResponse.error(errorMessage));
    }

    // Path variable or request param validation (e.g. @Validated)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<StandardResponse<Void>> handleConstraintViolation(ConstraintViolationException ex) {
        String errorMessages = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        return ResponseEntity.badRequest().body(StandardResponse.error(errorMessages));
    }

    // File upload too large
    @ExceptionHandler({MaxUploadSizeExceededException.class, MultipartException.class, IllegalStateException.class})
    public ResponseEntity<StandardResponse<Void>> handleFileUploadException(Exception ex) {
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
                .body(StandardResponse.error("Uploaded file exceeds the maximum allowed size of 10MB"));
    }

    // Any runtime exception
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<StandardResponse<Void>> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.badRequest().body(StandardResponse.error(ex.getMessage()));
    }

    // Catch-all for other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<StandardResponse<Void>> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(StandardResponse.error("An unexpected error occurred: " + ex.getMessage()));
    }

    @ExceptionHandler(ComplaintException.class)
    public ResponseEntity<StandardResponse<Object>> handleComplaint(ComplaintException ex) {
        return ResponseEntity.badRequest().body(StandardResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(MaintenancePaymentException.class)
    public ResponseEntity<StandardResponse<Object>> handleMaintenancePayment(MaintenancePaymentException ex) {
        return ResponseEntity.badRequest().body(StandardResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(MaintenanceException.class)
    public ResponseEntity<StandardResponse<Object>> handleMaintenance(MaintenanceException ex) {
        return ResponseEntity.badRequest().body(StandardResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(FlatException.class)
    public ResponseEntity<StandardResponse<Object>> handleFlat(FlatException ex) {
        return ResponseEntity.badRequest().body(StandardResponse.error(ex.getMessage()));
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<StandardResponse<Object>> handleUser(UserException ex) {
        return ResponseEntity.badRequest().body(StandardResponse.error(ex.getMessage()));
    }
}
package com.one.arpitInstituteAPI.controller;

import com.one.arpitInstituteAPI.dto.ReceiptRequest;
import com.one.arpitInstituteAPI.entity.Receipt;
import com.one.arpitInstituteAPI.repository.ReceiptRepository;
import com.one.arpitInstituteAPI.response.StandardResponse;
import com.one.arpitInstituteAPI.service.ReceiptService;
import com.one.arpitInstituteAPI.logger.DefaultLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.util.Optional;

@RestController
@RequestMapping("/receipts")
@Tag(name = "Receipt Management", description = "APIs for handling fee receipts")
public class ReceiptController {

    private static final String CLASSNAME = "ReceiptController";
    private static final DefaultLogger LOGGER = new DefaultLogger(ReceiptController.class);

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private ReceiptRepository receiptRepository;

    @PostMapping
    @Operation(summary = "Create a new receipt", description = "Generates and stores a new fee receipt")
    public ResponseEntity<StandardResponse<Receipt>> createReceipt(@Valid @RequestBody ReceiptRequest request) {
        String method = "createReceipt";
        try {
            String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
            LOGGER.infoLog(CLASSNAME, method, "Creating receipt for student: " + request.getStudentName() + ", by user: " + currentUser);
            Receipt receipt = receiptService.createReceipt(request, currentUser);
            LOGGER.infoLog(CLASSNAME, method, "Receipt created successfully with ID: " + receipt.getId());
            return new ResponseEntity<>(StandardResponse.success("Receipt generated successfully", receipt), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            LOGGER.errorLog(CLASSNAME, method, "Failed to create receipt: " + e.getMessage());
            return new ResponseEntity<>(StandardResponse.error(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get receipt by ID", description = "Fetch receipt details by receipt ID")
    public ResponseEntity<StandardResponse<Receipt>> getReceiptById(@PathVariable Long id) {
        String method = "getReceiptById";
        LOGGER.infoLog(CLASSNAME, method, "Fetching receipt with ID: " + id);
        Optional<Receipt> receiptOpt = receiptRepository.findById(id);
        return receiptOpt.map(receipt -> {
            LOGGER.infoLog(CLASSNAME, method, "Receipt fetched successfully for ID: " + id);
            return ResponseEntity.ok(StandardResponse.success("Receipt fetched", receipt));
        }).orElseGet(() -> {
            LOGGER.warnLog(CLASSNAME, method, "Receipt not found for ID: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StandardResponse.error("Receipt not found"));
        });
    }

    @GetMapping
    @Operation(summary = "Get all receipts", description = "Fetch all receipts with pagination and newest first")
    public ResponseEntity<StandardResponse<Page<Receipt>>> getAllReceipts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        String method = "getAllReceipts";
        LOGGER.infoLog(CLASSNAME, method, "Fetching receipts - Page: " + page + ", Size: " + size);

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")); // newest first
        Page<Receipt> receipts = receiptRepository.findAll(pageable);

        LOGGER.infoLog(CLASSNAME, method, "Fetched " + receipts.getTotalElements() + " receipts (page " + page + ")");
        return ResponseEntity.ok(StandardResponse.success("Receipts fetched", receipts));
    }

}
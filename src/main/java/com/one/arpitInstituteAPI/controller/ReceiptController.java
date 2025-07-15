package com.one.arpitInstituteAPI.controller;

import com.one.arpitInstituteAPI.dto.ReceiptRequest;
import com.one.arpitInstituteAPI.entity.Receipt;
import com.one.arpitInstituteAPI.repository.ReceiptRepository;
import com.one.arpitInstituteAPI.response.StandardResponse;
import com.one.arpitInstituteAPI.service.ReceiptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/receipts")
@Tag(name = "Receipt Management", description = "APIs for handling fee receipts")
public class ReceiptController {

    @Autowired
    private ReceiptService receiptService;

    @Autowired
    private ReceiptRepository receiptRepository;

    @PostMapping
    @Operation(summary = "Create a new receipt", description = "Generates and stores a new fee receipt")
    public ResponseEntity<StandardResponse<Receipt>> createReceipt(@RequestBody ReceiptRequest request) {
        String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        Receipt receipt = receiptService.createReceipt(request, currentUser);
        return new ResponseEntity<>(StandardResponse.success("Receipt generated successfully", receipt), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get receipt by ID", description = "Fetch receipt details by receipt ID")
    public ResponseEntity<StandardResponse<Receipt>> getReceiptById(@PathVariable Long id) {
        Optional<Receipt> receiptOpt = receiptRepository.findById(id);
        return receiptOpt.map(receipt -> ResponseEntity.ok(StandardResponse.success("Receipt fetched", receipt))).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(StandardResponse.error("Receipt not found")));
    }

    @GetMapping
    @Operation(summary = "Get all receipts", description = "Fetch all receipts")
    public ResponseEntity<StandardResponse<List<Receipt>>> getAllReceipts() {
        List<Receipt> receipts = receiptRepository.findAll();
        return ResponseEntity.ok(StandardResponse.success("All receipts fetched", receipts));
    }
}
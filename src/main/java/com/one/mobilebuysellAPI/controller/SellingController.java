package com.one.mobilebuysellAPI.controller;

import com.one.mobilebuysellAPI.dto.SellingDto;
import com.one.mobilebuysellAPI.logger.DefaultLogger;
import com.one.mobilebuysellAPI.response.StandardResponse;
import com.one.mobilebuysellAPI.service.SellingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/selling")
@Tag(name = "Selling Management", description = "APIs for managing mobile selling entries")
public class SellingController {

    private static final String CLASSNAME = "SellingController";
    private static final DefaultLogger LOGGER = new DefaultLogger(SellingController.class);

    private final SellingService sellingService;

    public SellingController(SellingService sellingService) {
        this.sellingService = sellingService;
    }

    @GetMapping
    @Operation(summary = "Get All Sell Entries", description = "Fetches all mobile sell entries")
    public ResponseEntity<StandardResponse<List<SellingDto>>> getAllSellings() {
        String method = "getAllSellings";
        LOGGER.infoLog(CLASSNAME, method, "Fetching all sell entries");

        List<SellingDto> list = sellingService.getAllSellings();
        LOGGER.debugLog(CLASSNAME, method, "Fetched " + list.size() + " selling entries");

        return ResponseEntity.ok(StandardResponse.success("Selling entries fetched successfully", list));
    }

    @PostMapping
    @Operation(summary = "Add New Sell Entry", description = "Add a new mobile sell entry with customer and pricing details")
    public ResponseEntity<StandardResponse<SellingDto>> addSelling(@RequestBody SellingDto sellingDto) {
        String method = "addSelling";
        LOGGER.infoLog(CLASSNAME, method, "Received new selling entry for invoice: " + sellingDto.getInvoiceNumber());

        try {
            SellingDto saved = sellingService.addSelling(sellingDto);
            LOGGER.debugLog(CLASSNAME, method, "Selling entry added successfully");
            return ResponseEntity.ok(StandardResponse.success("Selling entry added", saved));
        } catch (Exception e) {
            LOGGER.errorLog(CLASSNAME, method, "Error saving selling entry: " + e.getMessage());
            return ResponseEntity.internalServerError().body(StandardResponse.error("Failed to add selling entry"));
        }
    }
}
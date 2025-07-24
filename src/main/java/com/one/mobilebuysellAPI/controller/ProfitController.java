package com.one.mobilebuysellAPI.controller;

import com.one.mobilebuysellAPI.logger.DefaultLogger;
import com.one.mobilebuysellAPI.response.StandardResponse;
import com.one.mobilebuysellAPI.service.ProfitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Profit Management", description = "API for calculating total profit from sales")
public class ProfitController {

    private static final String CLASSNAME = "ProfitController";
    private static final DefaultLogger LOGGER = new DefaultLogger(ProfitController.class);

    private final ProfitService profitService;

    public ProfitController(ProfitService profitService) {
        this.profitService = profitService;
    }

    @GetMapping("/profit")
    @Operation(summary = "Get Total Profit", description = "Calculates and returns the total profit from all sold products")
    public ResponseEntity<StandardResponse<Double>> getTotalProfit() {
        String method = "getTotalProfit";
        LOGGER.infoLog(CLASSNAME, method, "Calculating total profit");

        try {
            double profit = profitService.getTotalProfit();
            LOGGER.debugLog(CLASSNAME, method, "Total profit calculated: " + profit);
            return ResponseEntity.ok(StandardResponse.success("Total profit calculated", profit));
        } catch (Exception e) {
            LOGGER.errorLog(CLASSNAME, method, "Error calculating profit: " + e.getMessage());
            return ResponseEntity.internalServerError().body(StandardResponse.error("Failed to calculate profit"));
        }
    }
}
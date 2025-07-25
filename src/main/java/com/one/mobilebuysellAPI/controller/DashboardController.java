package com.one.mobilebuysellAPI.controller;

import com.one.mobilebuysellAPI.dto.BuyingDto;
import com.one.mobilebuysellAPI.dto.DashboardCountSummaryDto;
import com.one.mobilebuysellAPI.dto.DashboardSellInfoDto;
import com.one.mobilebuysellAPI.dto.SellingDto;
import com.one.mobilebuysellAPI.logger.DefaultLogger;
import com.one.mobilebuysellAPI.response.StandardResponse;
import com.one.mobilebuysellAPI.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dashboard")
@Tag(name = "Dashboard", description = "APIs for retrieving dashboard data including bought and sold products")
public class DashboardController {

    private static final String CLASSNAME = "DashboardController";
    private static final DefaultLogger LOGGER = new DefaultLogger(DashboardController.class);

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/buying")
    @Operation(summary = "Get All Bought Products", description = "Fetches all products added in the buy list, optionally filtered by month and year")
    public ResponseEntity<StandardResponse<List<BuyingDto>>> getAllBoughtProducts(
            @RequestParam(value = "month", required = false) Integer month,
            @RequestParam(value = "year", required = false) Integer year) {

        String method = "getAllBoughtProducts";
        LOGGER.infoLog(CLASSNAME, method, "Fetching all bought products for month=" + month + ", year=" + year);

        List<BuyingDto> list = dashboardService.getAllBoughtProducts(month, year);
        LOGGER.debugLog(CLASSNAME, method, "Fetched " + list.size() + " bought products");

        return ResponseEntity.ok(StandardResponse.success("Bought products fetched successfully", list));
    }


    @GetMapping("/buying/imei/{imeiNumber}")
    @Operation(summary = "Get Bought Product by IMEI", description = "Fetch a specific bought product using the IMEI number")
    public ResponseEntity<StandardResponse<BuyingDto>> getBoughtProductByImei(@PathVariable String imeiNumber) {
        String method = "getBoughtProductByImei";
        LOGGER.infoLog(CLASSNAME, method, "Fetching bought product by IMEI: " + imeiNumber);

        try {
            BuyingDto result = dashboardService.getBoughtProductByImei(imeiNumber);
            LOGGER.debugLog(CLASSNAME, method, "Bought product found for IMEI: " + imeiNumber);
            return ResponseEntity.ok(StandardResponse.success("Bought product fetched successfully", result));
        } catch (Exception e) {
            LOGGER.errorLog(CLASSNAME, method, "Error fetching bought product by IMEI: " + e.getMessage());
            return ResponseEntity.badRequest().body(StandardResponse.error("Product not found for IMEI: " + imeiNumber));
        }
    }

    @GetMapping("/selling")
    @Operation(summary = "Get All Sold Products", description = "Fetches all products listed as sold, optionally filtered by month and year")
    public ResponseEntity<StandardResponse<List<SellingDto>>> getAllSoldProducts(
            @RequestParam(value = "month", required = false) Integer month,
            @RequestParam(value = "year", required = false) Integer year) {

        String method = "getAllSoldProducts";
        LOGGER.infoLog(CLASSNAME, method, "Fetching all sold products for month=" + month + ", year=" + year);

        List<SellingDto> list = dashboardService.getAllSoldProducts(month, year);
        LOGGER.debugLog(CLASSNAME, method, "Fetched " + list.size() + " sold products");

        return ResponseEntity.ok(StandardResponse.success("Sold products fetched successfully", list));
    }


    @GetMapping("/selling/profit-summary")
    @Operation(summary = "Get Sell Summary with Profit", description = "Fetch sold product details along with profit info, filtered by month and year")
    public ResponseEntity<StandardResponse<List<DashboardSellInfoDto>>> getSellSummaryWithProfit(
            @RequestParam(value = "month", required = false) Integer month,
            @RequestParam(value = "year", required = false) Integer year) {

        String method = "getSellSummaryWithProfit";
        LOGGER.infoLog(CLASSNAME, method, "Fetching selling summary with profit for month: " + month + ", year: " + year);

        List<DashboardSellInfoDto> summaryList = dashboardService.getSellInfoSummaryWithProfit(month, year);

        LOGGER.debugLog(CLASSNAME, method, "Fetched " + summaryList.size() + " records");

        return ResponseEntity.ok(StandardResponse.success("Selling summary with profit fetched", summaryList));
    }

    @GetMapping("/summary/count")
    @Operation(summary = "Get Total Buying & Selling Count", description = "Returns the total number of bought and sold products, optionally filtered by month and year")
    public ResponseEntity<StandardResponse<DashboardCountSummaryDto>> getTotalBuySellCount(
            @RequestParam(value = "month", required = false) Integer month,
            @RequestParam(value = "year", required = false) Integer year) {

        String method = "getTotalBuySellCount";
        LOGGER.infoLog(CLASSNAME, method, "Fetching total buy/sell count for month=" + month + ", year=" + year);

        DashboardCountSummaryDto summary = dashboardService.getBuySellCountSummary(month, year);
        LOGGER.debugLog(CLASSNAME, method, "Summary: " + summary.getTotalBuyingCount() + " buys, " + summary.getTotalSellingCount() + " sells");

        return ResponseEntity.ok(StandardResponse.success("Count summary fetched", summary));
    }


}
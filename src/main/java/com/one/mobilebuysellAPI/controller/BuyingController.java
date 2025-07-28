package com.one.mobilebuysellAPI.controller;

import com.one.mobilebuysellAPI.dto.BuyingDto;
import com.one.mobilebuysellAPI.exception.BuyingException;
import com.one.mobilebuysellAPI.logger.DefaultLogger;
import com.one.mobilebuysellAPI.response.StandardResponse;
import com.one.mobilebuysellAPI.service.BuyingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/buying")
@Tag(name = "Buying Management", description = "APIs for managing mobile buying entries")
public class BuyingController {

    private static final String CLASSNAME = "BuyingController";
    private static final DefaultLogger LOGGER = new DefaultLogger(BuyingController.class);

    private final BuyingService buyingService;

    public BuyingController(BuyingService buyingService) {
        this.buyingService = buyingService;
    }

    @GetMapping
    @Operation(summary = "Get All Buy Entries", description = "Fetches all mobile buy entries")
    public ResponseEntity<StandardResponse<List<BuyingDto>>> getAllBuyings() {
        String method = "getAllBuyings";
        LOGGER.infoLog(CLASSNAME, method, "Fetching all buying entries");

        List<BuyingDto> list = buyingService.getAllBuyings();
        LOGGER.debugLog(CLASSNAME, method, "Fetched " + list.size() + " entries");

        return ResponseEntity.ok(StandardResponse.success("Buyings fetched successfully", list));
    }

    @GetMapping("/imei/{imeiNumber}")
    @Operation(summary = "Get Buy Entry by IMEI", description = "Fetch a specific buy entry using IMEI number")
    public ResponseEntity<StandardResponse<BuyingDto>> getByImeiNumber(@PathVariable String imeiNumber) {
        String method = "getByImeiNumber";
        LOGGER.infoLog(CLASSNAME, method, "Fetching buying entry by IMEI: " + imeiNumber);

        try {
            BuyingDto result = buyingService.getByImeiNumber(imeiNumber);
            LOGGER.debugLog(CLASSNAME, method, "Entry found for IMEI: " + imeiNumber);
            return ResponseEntity.ok(StandardResponse.success("Entry fetched", result));
        } catch (Exception e) {
            LOGGER.errorLog(CLASSNAME, method, "Error fetching by IMEI: " + e.getMessage());
            return ResponseEntity.badRequest().body(StandardResponse.error("Buying entry not found for IMEI: " + imeiNumber));
        }
    }

    @PostMapping
    @Operation(summary = "Add New Buy Entry", description = "Add a new mobile buy entry with model, color, cost, and more")
    public ResponseEntity<StandardResponse<BuyingDto>> addBuying(@Valid @RequestBody BuyingDto buyingDto) {
        String method = "addBuying";
        LOGGER.infoLog(CLASSNAME, method, "Received new buying entry for model: " + buyingDto.getModelNumber());

        try {
            BuyingDto saved = buyingService.addBuying(buyingDto);
            LOGGER.debugLog(CLASSNAME, method, "Buying entry added successfully");
            return ResponseEntity.ok(StandardResponse.success("Buying entry added", saved));
        } catch (BuyingException e) {
            LOGGER.errorLog(CLASSNAME, method, "Error saving buying entry: " + e.getMessage());
            return ResponseEntity.badRequest().body(StandardResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/unsold")
    @Operation(summary = "Get Unsold Phones", description = "Returns list of phones that are not yet sold")
    public ResponseEntity<StandardResponse<List<BuyingDto>>> getUnsoldPhones() {
        String method = "getUnsoldPhones";
        LOGGER.infoLog(CLASSNAME, method, "Fetching unsold phones");

        List<BuyingDto> list = buyingService.getUnsoldPhones();
        LOGGER.debugLog(CLASSNAME, method, "Fetched " + list.size() + " unsold phones");

        return ResponseEntity.ok(StandardResponse.success("Unsold phones fetched", list));
    }

}
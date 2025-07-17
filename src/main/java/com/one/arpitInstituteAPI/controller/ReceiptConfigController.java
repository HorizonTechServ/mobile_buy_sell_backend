package com.one.arpitInstituteAPI.controller;

import com.one.arpitInstituteAPI.entity.ReceiptConfig;
import com.one.arpitInstituteAPI.logger.DefaultLogger;
import com.one.arpitInstituteAPI.response.StandardResponse;
import com.one.arpitInstituteAPI.service.ReceiptConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/receipt-config")
@Tag(name = "Receipt Configuration", description = "Manage default receipt fee configurations")
public class ReceiptConfigController {

    private static final String CLASSNAME = "ReceiptConfigController";
    private static final DefaultLogger LOGGER = new DefaultLogger(ReceiptConfigController.class);

    @Autowired
    private ReceiptConfigService configService;

    @GetMapping
    @Operation(summary = "Get current config")
    public ResponseEntity<StandardResponse<List<ReceiptConfig>>> getAll() {
        LOGGER.infoLog(CLASSNAME, "getAll", "Fetching receipt config");
        List<ReceiptConfig> list = configService.getAll();
        return ResponseEntity.ok(StandardResponse.success("Config fetched", list));
    }

    @PostMapping
    @Operation(summary = "Create new config")
    public ResponseEntity<StandardResponse<ReceiptConfig>> create(@RequestBody ReceiptConfig config) {
        LOGGER.infoLog(CLASSNAME, "create", "Creating receipt config");
        try {
            ReceiptConfig saved = configService.create(config);
            return new ResponseEntity<>(StandardResponse.success("Config created", saved), HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            LOGGER.errorLog(CLASSNAME, "create", e.getMessage());
            return ResponseEntity.badRequest().body(StandardResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update config")
    public ResponseEntity<StandardResponse<ReceiptConfig>> update(@PathVariable Long id, @RequestBody ReceiptConfig config) {
        LOGGER.infoLog(CLASSNAME, "update", "Updating receipt config with ID: " + id);
        try {
            ReceiptConfig updated = configService.update(id, config);
            return ResponseEntity.ok(StandardResponse.success("Config updated", updated));
        } catch (IllegalArgumentException e) {
            LOGGER.errorLog(CLASSNAME, "update", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StandardResponse.error(e.getMessage()));
        }
    }
}
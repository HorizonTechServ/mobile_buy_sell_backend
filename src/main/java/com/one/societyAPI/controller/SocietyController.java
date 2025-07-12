package com.one.societyAPI.controller;

import com.one.societyAPI.dto.CreateSocietyRequest;
import com.one.societyAPI.dto.PatchSocietyRequest;
import com.one.societyAPI.entity.Society;
import com.one.societyAPI.logger.DefaultLogger;
import com.one.societyAPI.response.StandardResponse;
import com.one.societyAPI.service.SocietyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/societies")
@Tag(name = "Society Management", description = "APIs for managing a Society")
public class SocietyController {

    private static final String CLASSNAME = "SocietyController";
    private static final DefaultLogger LOGGER = new DefaultLogger(SocietyController.class);

    @Autowired
    private SocietyService societyService;

    @PostMapping
    @Operation(summary = "Super admin Can create new Society", description = "add new society")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<StandardResponse<Society>> createSociety(@RequestBody CreateSocietyRequest request) {
        String method = "createSociety";
        LOGGER.infoLog(CLASSNAME, method, "Received request to create a new society with name: " + request.getName());

        Society society = societyService.createSociety(request);

        LOGGER.infoLog(CLASSNAME, method, "Successfully created society with ID: ", society.getId());
        return ResponseEntity.ok(StandardResponse.success("Society created successfully", society));
    }

    @GetMapping
    @Operation(summary = "Get all Society", description = "Get all society")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<StandardResponse<List<Society>>> getAllSocieties() {
        String method = "getAllSocieties";
        LOGGER.infoLog(CLASSNAME, method, "Received request to fetch all societies.");

        List<Society> societies = societyService.getAllSocieties();

        LOGGER.infoLog(CLASSNAME, method, "Successfully fetched " + societies.size() + "societies");
        return ResponseEntity.ok(StandardResponse.success("Societies fetched successfully", societies));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get Society by ID", description = "Fetch a society by its ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<StandardResponse<Society>> getSocietyById(@PathVariable Long id) {
        String method = "getSocietyById";
        LOGGER.infoLog(CLASSNAME, method, "Received request to fetch society with ID: ", id);

        Society society = societyService.getSocietyById(id);
        LOGGER.infoLog(CLASSNAME, method, "Successfully fetched society with ID: ", id);

        return ResponseEntity.ok(StandardResponse.success("Society fetched successfully", society));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete Society by ID", description = "Delete a society by its ID")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<StandardResponse<String>> deleteSociety(@PathVariable Long id) {
        String method = "deleteSociety";
        LOGGER.infoLog(CLASSNAME, method, "Received request to delete society with ID: ", id);

        societyService.deleteSociety(id);
        LOGGER.infoLog(CLASSNAME, method, "Successfully deleted society with ID: ", id);

        return ResponseEntity.ok(StandardResponse.success("Society deleted successfully", id.toString()));
    }

    @GetMapping("/flats/{societyId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Get flats by society ID", description = "Returns available and assigned flats for a society")
    public ResponseEntity<StandardResponse<Map<String, Object>>> getFlatsBySocietyId(@PathVariable Long societyId) {
        Map<String, Object> flatMap = societyService.getFlatsBySocietyId(societyId);
        return ResponseEntity.ok(StandardResponse.success("Flats fetched successfully", flatMap));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update existing Society", description = "Update a society by its ID")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Society> patchSociety(
            @PathVariable Long id,
            @RequestBody PatchSocietyRequest request) {
        Society updatedSociety = societyService.patchSociety(id, request);
        return ResponseEntity.ok(updatedSociety);
    }
}
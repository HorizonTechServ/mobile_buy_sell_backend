package com.one.societyAPI.controller;

import com.one.societyAPI.dto.ComplaintDTO;
import com.one.societyAPI.logger.DefaultLogger;
import com.one.societyAPI.response.StandardResponse;
import com.one.societyAPI.service.ComplaintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/complaints")
@Tag(name = "Complaint Management", description = "APIs for managing complaints")
public class ComplaintController {

    private static final String CLASSNAME = "ComplaintController";
    private static final DefaultLogger LOGGER = new DefaultLogger(ComplaintController.class);

    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }


    @PostMapping("/create")
    @Operation(summary = "Add a complaint", description = "Add a new complaint")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'USER')")
    public ResponseEntity<StandardResponse<ComplaintDTO>> createComplaint(
            @RequestParam Long userId,
            @RequestParam String description
    ) {
        ComplaintDTO complaint = complaintService.createComplaint(userId, description);
        return ResponseEntity.ok(StandardResponse.success("Complaint created successfully", complaint));
    }


    @PostMapping("/resolve")
    @Operation(summary = "Resolve complaint", description = "Super Admin and Admin can resolve complaints")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<StandardResponse<ComplaintDTO>> resolveComplaint(
            @RequestParam Long complaintId,
            @RequestParam Long resolverId
    ) {
        ComplaintDTO resolved = complaintService.resolveComplaint(complaintId, resolverId);
        return ResponseEntity.ok(StandardResponse.success("Complaint resolved successfully", resolved));
    }


    @GetMapping("/by-user/{userId}")
    @Operation(summary = "Get Complaint By User ID", description = "Get complaints by user ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'USER')")
    public ResponseEntity<StandardResponse<List<ComplaintDTO>>> getByUser(@PathVariable Long userId) {
        List<ComplaintDTO> complaints = complaintService.getComplaintsByUser(userId);
        return ResponseEntity.ok(StandardResponse.success("Fetched complaints by user", complaints));
    }

    @GetMapping("/by-society/{societyId}")
    @Operation(summary = "Get Complaint By Society ID", description = "Get complaints by society ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'USER')")
    public ResponseEntity<StandardResponse<List<ComplaintDTO>>> getBySociety(@PathVariable Long societyId) {
        List<ComplaintDTO> complaints = complaintService.getComplaintsBySociety(societyId);
        return ResponseEntity.ok(StandardResponse.success("Fetched complaints by society", complaints));
    }

    @GetMapping("/open/{societyId}")
    @Operation(summary = "List of Open Complaints", description = "Returns all open complaints for a society")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'USER')")
    public ResponseEntity<StandardResponse<List<ComplaintDTO>>> getOpenComplaints(@PathVariable Long societyId) {
        List<ComplaintDTO> openComplaints = complaintService.getOpenComplaintsBySociety(societyId);
        return ResponseEntity.ok(StandardResponse.success("Fetched open complaints", openComplaints));
    }

    @GetMapping("/resolved/{societyId}")
    @Operation(summary = "List of Resolved Complaints", description = "Returns all resolved complaints for a society")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<StandardResponse<List<ComplaintDTO>>> getResolvedComplaints(@PathVariable Long societyId) {
        List<ComplaintDTO> resolvedComplaints = complaintService.getResolvedComplaintsBySociety(societyId);
        return ResponseEntity.ok(StandardResponse.success("Fetched resolved complaints", resolvedComplaints));
    }
}
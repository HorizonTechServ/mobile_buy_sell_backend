package com.one.societyAPI.controller;

import com.one.societyAPI.dto.ComplaintDTO;
import com.one.societyAPI.service.ComplaintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/complaints")
@Tag(name = "Complaint Management", description = "APIs for managing a complaints")
public class ComplaintController {

    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @PostMapping("/create")
    @Operation(summary = "Add a complaint", description = "add new complaint")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'USER')")
    public ResponseEntity<ComplaintDTO> createComplaint(
            @RequestParam Long userId,
            @RequestParam String description
    ) {
        return ResponseEntity.ok(complaintService.createComplaint(userId, description));
    }

    @PostMapping("/resolve")
    @Operation(summary = "Resolve complaint", description = "Super Admin and Admin Can Resolve Complaint")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ComplaintDTO> resolveComplaint(
            @RequestParam Long complaintId,
            @RequestParam Long resolverId
    ) {
        return ResponseEntity.ok(complaintService.resolveComplaint(complaintId, resolverId));
    }

    @GetMapping("/by-user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'USER')")
    @Operation(summary = "Get Complaint By User ID", description = "Get Complaint by User ID ")
    public ResponseEntity<List<ComplaintDTO>> getByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(complaintService.getComplaintsByUser(userId));
    }

    @GetMapping("/by-society/{societyId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'USER')")
    @Operation(summary = "Get Complaint By Society ID", description = "Get Complaint by Society ID ")
    public ResponseEntity<List<ComplaintDTO>> getBySociety(@PathVariable Long societyId) {
        return ResponseEntity.ok(complaintService.getComplaintsBySociety(societyId));
    }
}
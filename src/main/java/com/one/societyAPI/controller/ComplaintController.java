package com.one.societyAPI.controller;

import com.one.societyAPI.dto.ComplaintDTO;
import com.one.societyAPI.entity.ChatMessage;
import com.one.societyAPI.logger.DefaultLogger;
import com.one.societyAPI.repository.ChatMessageRepository;
import com.one.societyAPI.response.StandardResponse;
import com.one.societyAPI.service.ComplaintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/complaints")
@Tag(name = "Complaint Management", description = "APIs for managing complaints")
public class ComplaintController {

    private static final String CLASSNAME = "ComplaintController";

    private static final DefaultLogger LOGGER = new DefaultLogger(ComplaintController.class);

    private final ComplaintService complaintService;

    private final ChatMessageRepository chatMessageRepository;

    public ComplaintController(ComplaintService complaintService, ChatMessageRepository chatMessageRepository) {
        this.complaintService = complaintService;
        this.chatMessageRepository = chatMessageRepository;
    }


    @PostMapping(value = "/create", consumes = {"multipart/form-data"})
    @Operation(summary = "Add a complaint", description = "Add a new complaint with optional image")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'USER')")
    public ResponseEntity<StandardResponse<ComplaintDTO>> createComplaint(
            @RequestParam Long userId,
            @RequestParam String description,
            @RequestParam(required = false) MultipartFile image
    ) {
        String method = "createComplaint";
        LOGGER.infoLog(CLASSNAME, method, "Received complaint creation request from userId: ", userId);

        ComplaintDTO complaint = complaintService.createComplaint(userId, description, image);

        LOGGER.infoLog(CLASSNAME, method, "Complaint created successfully with ID: ", complaint.id());

        return ResponseEntity.ok(StandardResponse.success("Complaint created successfully", complaint));
    }


    @PostMapping(value = "/resolve", consumes = {"multipart/form-data"})
    @Operation(summary = "Resolve complaint", description = "Admin and User can resolve complaints with optional image and notes")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'USER')")
    public ResponseEntity<StandardResponse<ComplaintDTO>> resolveComplaint(
            @RequestParam Long complaintId,
            @RequestParam Long resolverId,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) MultipartFile image
    ) {
        String method = "resolveComplaint";
        LOGGER.infoLog(CLASSNAME, method, "Attempt to resolve complaintId: " + complaintId + " by resolverId: ", resolverId);

        ComplaintDTO resolved = complaintService.resolveComplaint(complaintId, resolverId, description, image);

        LOGGER.infoLog(CLASSNAME, method, "Complaint resolved successfully. ID: ", resolved.id());
        return ResponseEntity.ok(StandardResponse.success("Complaint resolved successfully", resolved));
    }


    @GetMapping("/by-user/{userId}")
    @Operation(summary = "Get Complaint By User ID", description = "Get complaints by user ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'USER')")
    public ResponseEntity<StandardResponse<List<ComplaintDTO>>> getByUser(@PathVariable Long userId) {
        String method = "getByUser";
        LOGGER.infoLog(CLASSNAME, method, "Fetching complaints for userId: ", userId);

        List<ComplaintDTO> complaints = complaintService.getComplaintsByUser(userId);

        LOGGER.debugLog(CLASSNAME, method, "Found " +complaints.size() +" complaints for userId: ", + userId);
        return ResponseEntity.ok(StandardResponse.success("Fetched complaints by user", complaints));
    }

    @GetMapping("/by-society/{societyId}")
    @Operation(summary = "Get Complaint By Society ID", description = "Get complaints by society ID")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'USER')")
    public ResponseEntity<StandardResponse<List<ComplaintDTO>>> getBySociety(@PathVariable Long societyId) {
        String method = "getBySociety";
        LOGGER.infoLog(CLASSNAME, method, "Fetching complaints for societyId: ", societyId);

        List<ComplaintDTO> complaints = complaintService.getComplaintsBySociety(societyId);

        LOGGER.debugLog(CLASSNAME, method, "Found " + complaints.size() + " complaints for societyId: ", + societyId);
        return ResponseEntity.ok(StandardResponse.success("Fetched complaints by society", complaints));
    }

    @GetMapping("/open/{societyId}")
    @Operation(summary = "List of Open Complaints", description = "Returns all open complaints for a society")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'USER')")
    public ResponseEntity<StandardResponse<List<ComplaintDTO>>> getOpenComplaints(@PathVariable Long societyId) {
        String method = "getOpenComplaints";
        LOGGER.infoLog(CLASSNAME, method, "Fetching open complaints for societyId: ", societyId);

        List<ComplaintDTO> openComplaints = complaintService.getOpenComplaintsBySociety(societyId);

        LOGGER.debugLog(CLASSNAME, method, "Found " +openComplaints.size()+ " open complaints for societyId: ", + societyId);
        return ResponseEntity.ok(StandardResponse.success("Fetched open complaints", openComplaints));
    }

    @GetMapping("/resolved/{societyId}")
    @Operation(summary = "List of Resolved Complaints", description = "Returns all resolved complaints for a society")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<StandardResponse<List<ComplaintDTO>>> getResolvedComplaints(@PathVariable Long societyId) {
        String method = "getResolvedComplaints";
        LOGGER.infoLog(CLASSNAME, method, "Fetching resolved complaints for societyId: ", societyId);

        List<ComplaintDTO> resolvedComplaints = complaintService.getResolvedComplaintsBySociety(societyId);

        LOGGER.debugLog(CLASSNAME, method, "Found " +resolvedComplaints.size() + " resolved complaints for societyId: ",  + societyId);
        return ResponseEntity.ok(StandardResponse.success("Fetched resolved complaints", resolvedComplaints));
    }

    @GetMapping("/chat/{complaintId}")
    @Operation(summary = "Get chat messages by complaint", description = "Returns chat history for the given complaint")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'USER')")
    public ResponseEntity<StandardResponse<List<ChatMessage>>> getChatHistory(@PathVariable Long complaintId) {
        List<ChatMessage> messages = chatMessageRepository.findByComplaintIdOrderByTimestampAsc(complaintId);
        return ResponseEntity.ok(StandardResponse.success("Fetched chat history", messages));
    }

}
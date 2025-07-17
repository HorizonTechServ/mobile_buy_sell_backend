package com.one.arpitInstituteAPI.controller;

import com.one.arpitInstituteAPI.entity.Branch;
import com.one.arpitInstituteAPI.logger.DefaultLogger;
import com.one.arpitInstituteAPI.response.StandardResponse;
import com.one.arpitInstituteAPI.service.BranchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/branches")
@Tag(name = "Branch Management", description = "CRUD APIs for branches")
class BranchController {

    private static final String CLASSNAME = "BranchController";
    private static final DefaultLogger LOGGER = new DefaultLogger(BranchController.class);

    @Autowired
    private BranchService branchService;

    @PostMapping
    @Operation(summary = "Add new branch")
    public ResponseEntity<StandardResponse<Branch>> create(@RequestBody Branch branch) {
        String method = "create";
        LOGGER.infoLog(CLASSNAME, method, "Creating branch: " + branch.getName());
        try {
            Branch saved = branchService.create(branch);
            return ResponseEntity.status(HttpStatus.CREATED).body(StandardResponse.success("Branch created", saved));
        } catch (IllegalArgumentException e) {
            LOGGER.errorLog(CLASSNAME, method, "Error creating branch: " + e.getMessage());
            return ResponseEntity.badRequest().body(StandardResponse.error(e.getMessage()));
        }
    }

    @GetMapping
    @Operation(summary = "Get all branches")
    public ResponseEntity<StandardResponse<List<Branch>>> getAll() {
        String method = "getAll";
        LOGGER.infoLog(CLASSNAME, method, "Fetching all branches");
        List<Branch> list = branchService.getAll();
        return ResponseEntity.ok(StandardResponse.success("Branches fetched", list));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete branch by ID")
    public ResponseEntity<StandardResponse<Void>> delete(@PathVariable Long id) {
        String method = "delete";
        LOGGER.infoLog(CLASSNAME, method, "Deleting branch with ID: " + id);
        branchService.delete(id);
        return ResponseEntity.ok(StandardResponse.success("Branch deleted", null));
    }
}
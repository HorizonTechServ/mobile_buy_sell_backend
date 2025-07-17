package com.one.arpitInstituteAPI.controller;

import com.one.arpitInstituteAPI.entity.Semester;
import com.one.arpitInstituteAPI.logger.DefaultLogger;
import com.one.arpitInstituteAPI.response.StandardResponse;
import com.one.arpitInstituteAPI.service.SemesterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/semesters")
@Tag(name = "Semester Management", description = "CRUD APIs for semesters")
public class SemesterController {

    private static final String CLASSNAME = "SemesterController";
    private static final DefaultLogger LOGGER = new DefaultLogger(SemesterController.class);

    @Autowired
    private SemesterService semesterService;

    @PostMapping
    @Operation(summary = "Add new semester")
    public ResponseEntity<StandardResponse<Semester>> create(@RequestBody Semester semester) {
        String method = "create";
        LOGGER.infoLog(CLASSNAME, method, "Creating semester: " + semester.getName());
        try {
            Semester saved = semesterService.create(semester);
            return ResponseEntity.status(HttpStatus.CREATED).body(StandardResponse.success("Semester created", saved));
        } catch (IllegalArgumentException e) {
            LOGGER.errorLog(CLASSNAME, method, "Error creating semester: " + e.getMessage());
            return ResponseEntity.badRequest().body(StandardResponse.error(e.getMessage()));
        }
    }

    @GetMapping
    @Operation(summary = "Get all semesters")
    public ResponseEntity<StandardResponse<List<Semester>>> getAll() {
        String method = "getAll";
        LOGGER.infoLog(CLASSNAME, method, "Fetching all semesters");
        List<Semester> list = semesterService.getAll();
        return ResponseEntity.ok(StandardResponse.success("Semesters fetched", list));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete semester by ID")
    public ResponseEntity<StandardResponse<Void>> delete(@PathVariable Long id) {
        String method = "delete";
        LOGGER.infoLog(CLASSNAME, method, "Deleting semester with ID: " + id);
        semesterService.delete(id);
        return ResponseEntity.ok(StandardResponse.success("Semester deleted", null));
    }
}
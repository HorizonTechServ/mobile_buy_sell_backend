package com.one.arpitInstituteAPI.controller;

import com.one.arpitInstituteAPI.entity.Department;
import com.one.arpitInstituteAPI.logger.DefaultLogger;
import com.one.arpitInstituteAPI.response.StandardResponse;
import com.one.arpitInstituteAPI.service.DepartmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
@Tag(name = "Department Management", description = "CRUD APIs for departments")
public class DepartmentController {

    private static final String CLASSNAME = "DepartmentController";
    private static final DefaultLogger LOGGER = new DefaultLogger(DepartmentController.class);

    @Autowired
    private DepartmentService departmentService;

    @PostMapping
    @Operation(summary = "Add new department")
    public ResponseEntity<StandardResponse<Department>> create(@RequestBody Department department) {
        String method = "create";
        LOGGER.infoLog(CLASSNAME, method, "Creating department: " + department.getName());
        try {
            Department saved = departmentService.create(department);
            return ResponseEntity.status(HttpStatus.CREATED).body(StandardResponse.success("Department created", saved));
        } catch (IllegalArgumentException e) {
            LOGGER.errorLog(CLASSNAME, method, "Error creating department: " + e.getMessage());
            return ResponseEntity.badRequest().body(StandardResponse.error(e.getMessage()));
        }
    }

    @GetMapping
    @Operation(summary = "Get all departments")
    public ResponseEntity<StandardResponse<List<Department>>> getAll() {
        String method = "getAll";
        LOGGER.infoLog(CLASSNAME, method, "Fetching all departments");
        List<Department> list = departmentService.getAll();
        return ResponseEntity.ok(StandardResponse.success("Departments fetched", list));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get department by ID")
    public ResponseEntity<StandardResponse<Department>> getById(@PathVariable Long id) {
        String method = "getById";
        LOGGER.infoLog(CLASSNAME, method, "Fetching department by ID: " + id);
        Department department = departmentService.getById(id);
        return ResponseEntity.ok(StandardResponse.success("Department fetched", department));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update department by ID")
    public ResponseEntity<StandardResponse<Department>> update(@PathVariable Long id, @RequestBody Department updatedDept) {
        String method = "update";
        LOGGER.infoLog(CLASSNAME, method, "Updating department with ID: " + id);
        Department updated = departmentService.update(id, updatedDept);
        return ResponseEntity.ok(StandardResponse.success("Department updated", updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete department by ID")
    public ResponseEntity<StandardResponse<Void>> delete(@PathVariable Long id) {
        String method = "delete";
        LOGGER.infoLog(CLASSNAME, method, "Deleting department with ID: " + id);
        departmentService.delete(id);
        return ResponseEntity.ok(StandardResponse.success("Department deleted", null));
    }
}
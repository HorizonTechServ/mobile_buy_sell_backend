package com.one.arpitInstituteAPI.controller;

import com.one.arpitInstituteAPI.dto.StudentNameDTO;
import com.one.arpitInstituteAPI.entity.Student;
import com.one.arpitInstituteAPI.repository.StudentRepository;
import com.one.arpitInstituteAPI.response.StandardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
@Tag(name = "Student Management", description = "APIs for managing student")
public class StudentController {

    private final StudentRepository studentRepository;



    @GetMapping
    @Operation(summary = "Get all students", description = "Fetches all unique students with pagination and newest first")
    public ResponseEntity<StandardResponse<Page<Student>>> getAllStudents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id")); // or "id" if createdAt doesn't exist
        Page<Student> students = studentRepository.findAll(pageable);
        return ResponseEntity.ok(StandardResponse.success("Student list fetched successfully", students));
    }


    @GetMapping("/drop-down")
    @Operation(summary = "Get all students for prefill data", description = "Fetches all unique students")
    public ResponseEntity<StandardResponse<List<Student>>> getAllStudentsForDropDown() {
        List<Student> students = studentRepository.findAll();
        return ResponseEntity.ok(StandardResponse.success("Student list fetched successfully", students));
    }

    @GetMapping("/names")
    @Operation(summary = "Get all student names", description = "Fetches only student names")
    public ResponseEntity<StandardResponse<List<StudentNameDTO>>> getAllStudentNames() {
        List<StudentNameDTO> names = studentRepository.findAllStudentNames();
        return ResponseEntity.ok(StandardResponse.success("Student names fetched successfully", names));
    }
}
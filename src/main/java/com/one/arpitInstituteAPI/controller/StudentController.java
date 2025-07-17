package com.one.arpitInstituteAPI.controller;

import com.one.arpitInstituteAPI.entity.Student;
import com.one.arpitInstituteAPI.repository.StudentRepository;
import com.one.arpitInstituteAPI.response.StandardResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentRepository studentRepository;

    @GetMapping
    @Operation(summary = "Get all students", description = "Fetches all unique students")
    public ResponseEntity<StandardResponse<List<Student>>> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return ResponseEntity.ok(StandardResponse.success("Student list fetched successfully", students));
    }
}
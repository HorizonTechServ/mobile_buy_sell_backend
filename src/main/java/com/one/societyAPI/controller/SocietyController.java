package com.one.societyAPI.controller;

import com.one.societyAPI.dto.CreateSocietyRequest;
import com.one.societyAPI.entity.Society;
import com.one.societyAPI.service.SocietyService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/societies")
@Tag(name = "Society Management", description = "APIs for managing a Society")
public class SocietyController {

    @Autowired
    private SocietyService societyService;

    @PostMapping
    @Operation(summary = "Super admin Can create new Society", description = "add new society")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Society> createSociety(@RequestBody CreateSocietyRequest request) {
        Society society = societyService.createSociety(request);
        return ResponseEntity.ok(society);
    }

    @GetMapping
    @Operation(summary = "get all Society", description = "get all society")
    public ResponseEntity<List<Society>> getAllSocieties() {
        return ResponseEntity.ok(societyService.getAllSocieties());
    }
}
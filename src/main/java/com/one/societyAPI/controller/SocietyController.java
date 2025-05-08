package com.one.societyAPI.controller;

import com.one.societyAPI.entity.Society;
import com.one.societyAPI.service.SocietyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/societies")
public class SocietyController {

    @Autowired
    private SocietyService societyService;

    // Super Admin or Admin
    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<Society> create(@RequestBody Society society) {
        return new ResponseEntity<>(societyService.createSociety(society), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Society>> getAll() {
        return ResponseEntity.ok(societyService.getAllSocieties());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Society> getById(@PathVariable Long id) {
        return ResponseEntity.ok(societyService.getSocietyById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize(  "hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<Society> update(@PathVariable Long id, @RequestBody Society updated) {
        return ResponseEntity.ok(societyService.updateSociety(id, updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        societyService.deleteSociety(id);
        return ResponseEntity.noContent().build();
    }
}
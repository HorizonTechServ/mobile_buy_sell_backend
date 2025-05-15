package com.one.societyAPI.repository;

import com.one.societyAPI.entity.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
    List<Maintenance> findBySociety_Id(Long societyId);
}
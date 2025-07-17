package com.one.arpitInstituteAPI.repository;

import com.one.arpitInstituteAPI.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    boolean existsByName(String name);
}
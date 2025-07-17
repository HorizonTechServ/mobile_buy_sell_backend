package com.one.arpitInstituteAPI.repository;

import com.one.arpitInstituteAPI.entity.Semester;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SemesterRepository extends JpaRepository<Semester, Long> {
    boolean existsByName(String name);
}
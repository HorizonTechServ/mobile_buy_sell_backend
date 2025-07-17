package com.one.arpitInstituteAPI.repository;

import com.one.arpitInstituteAPI.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<Branch, Long> {
    boolean existsByName(String name);
}
package com.one.arpitInstituteAPI.repository;

import com.one.arpitInstituteAPI.entity.ReceiptConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReceiptConfigRepository extends JpaRepository<ReceiptConfig, Long> {
    Optional<ReceiptConfig> findTopByOrderByIdDesc();
}

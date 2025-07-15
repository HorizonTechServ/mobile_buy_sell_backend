package com.one.arpitInstituteAPI.repository;

import com.one.arpitInstituteAPI.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    Optional<Receipt> findByReceiptNo(String receiptNo);
}
package com.one.arpitInstituteAPI.repository;

import com.one.arpitInstituteAPI.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    Optional<Receipt> findByReceiptNo(String receiptNo);
    Optional<Receipt> findByStudentNameAndSemesterAndDepartment(String studentName, String semester, String department);

    @Query(value = "SELECT receipt_no FROM receipt ORDER BY receipt_no DESC LIMIT 1", nativeQuery = true)
    String findLastReceiptNo();
}
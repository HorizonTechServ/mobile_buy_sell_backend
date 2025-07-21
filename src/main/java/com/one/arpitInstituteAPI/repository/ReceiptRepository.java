package com.one.arpitInstituteAPI.repository;

import com.one.arpitInstituteAPI.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
    Optional<Receipt> findByReceiptNo(String receiptNo);
    Optional<Receipt> findByStudentNameAndSemesterAndDepartment(String studentName, String semester, String department);

    @Query(value = "SELECT receipt_no FROM receipt ORDER BY receipt_no DESC LIMIT 1", nativeQuery = true)
    String findLastReceiptNo();

    @Query("SELECT SUM(r.totalAmount) FROM Receipt r WHERE r.status = 'PAID'")
    Long sumTotalFeesPaid();

    List<Receipt> findTop10ByOrderByCreatedAtDesc();

    @Query("SELECT new map(FUNCTION('DATE_FORMAT', r.createdAt, '%b') as month, SUM(r.totalAmount) as amount) " +
            "FROM Receipt r WHERE r.status = 'PAID' " +
            "GROUP BY FUNCTION('DATE_FORMAT', r.createdAt, '%b'), FUNCTION('DATE_FORMAT', r.createdAt, '%m') " +
            "ORDER BY FUNCTION('DATE_FORMAT', r.createdAt, '%m')")
    List<Map<String, Object>> findMonthlyCollection();

    @Query("SELECT new map(r.paymentMode as mode, COUNT(r) as count) " +
            "FROM Receipt r GROUP BY r.paymentMode")
    List<Map<String, Object>> countByPaymentMode();

    List<Receipt> findByDate(LocalDate date);
}
package com.one.arpitInstituteAPI.controller;

import com.one.arpitInstituteAPI.entity.Receipt;
import com.one.arpitInstituteAPI.repository.ReceiptRepository;
import com.one.arpitInstituteAPI.repository.StudentRepository;
import com.one.arpitInstituteAPI.response.StandardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@Tag(name = "Dashboard", description = "APIs for dashboard statistics")
public class DashboardController {

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/summary")
    public ResponseEntity<StandardResponse<Map<String, Object>>> getDashboardSummary() {
        Map<String, Object> summary = new HashMap<>();
        summary.put("totalReceipts", receiptRepository.count());
        summary.put("totalAmountCollected", receiptRepository.sumTotalFeesPaid());
        summary.put("totalStudents", studentRepository.count());

        return ResponseEntity.ok(StandardResponse.success("Dashboard summary fetched", summary));
    }

    @GetMapping("/recent-receipts")
    @Operation(summary = "get Recent Receipts", description = "Returns Top 10 by Order by Created Desc")
    public ResponseEntity<StandardResponse<List<Receipt>>> getRecentReceipts() {
        List<Receipt> receipts = receiptRepository.findTop10ByOrderByCreatedAtDesc();
        return ResponseEntity.ok(StandardResponse.success("Recent receipts fetched", receipts));
    }

    @GetMapping("/monthly-collection")
    @Operation(summary = "Get monthly fee collection", description = "Returns collection data grouped by month")
    public ResponseEntity<StandardResponse<List<Map<String, Object>>>> getMonthlyCollection() {
        List<Map<String, Object>> monthlyData = receiptRepository.findMonthlyCollection();
        return ResponseEntity.ok(StandardResponse.success("Monthly collection fetched", monthlyData));
    }

    @GetMapping("/payment-mode-stats")
    @Operation(summary = "Payment Mode Stats", description = "Returns count and percentage of receipts per payment mode")
    public ResponseEntity<StandardResponse<List<Map<String, Object>>>> getPaymentModeStats() {
        List<Map<String, Object>> stats = receiptRepository.countByPaymentMode();

        // Calculate total count
        long total = stats.stream()
                .mapToLong(item -> (Long) item.get("count"))
                .sum();

        // Add percentage to each map entry
        for (Map<String, Object> item : stats) {
            long count = (Long) item.get("count");
            double percentage = (total > 0) ? (count * 100.0 / total) : 0;
            item.put("percentage", Math.round(percentage * 100.0) / 100.0); // round to 2 decimals
        }

        return ResponseEntity.ok(StandardResponse.success("Payment mode statistics fetched", stats));
    }


    @GetMapping("/by-date")
    @Operation(summary = "Get receipts by date", description = "Fetch receipts for a specific date (format: yyyy-MM-dd)")
    public ResponseEntity<StandardResponse<List<Receipt>>> getReceiptsByDate(
            @RequestParam("date") @org.springframework.format.annotation.DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date) {

        List<Receipt> receipts = receiptRepository.findByDate(date);
        return ResponseEntity.ok(StandardResponse.success("Receipts for date " + date + " fetched", receipts));
    }
}
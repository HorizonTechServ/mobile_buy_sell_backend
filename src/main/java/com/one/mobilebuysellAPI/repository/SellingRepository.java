package com.one.mobilebuysellAPI.repository;

import com.one.mobilebuysellAPI.dto.DashboardSellInfoDto;
import com.one.mobilebuysellAPI.entity.Selling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SellingRepository extends JpaRepository<Selling, Long> {

    @Query("SELECT s FROM Selling s WHERE FUNCTION('MONTH', s.sellDate) = :month AND FUNCTION('YEAR', s.sellDate) = :year")
    List<Selling> findByMonthAndYear(@Param("month") int month, @Param("year") int year);


    @Query("SELECT new com.one.mobilebuysellAPI.dto.DashboardSellInfoDto(" +
            "b.modelNumber, b.storage, b.color, b.purchaseCost, " +
            "s.sellDate, s.sellPrice, s.customerName, " +
            "CAST(s.sellPrice - b.purchaseCost AS double)) " +
            "FROM Selling s JOIN s.buying b")
    List<DashboardSellInfoDto> getSellingInfoWithProfit();

    boolean existsByInvoiceNumber(String invoiceNumber);

    @Query("SELECT new com.one.mobilebuysellAPI.dto.DashboardSellInfoDto(" +
            "b.modelNumber, b.storage, b.color, b.purchaseCost, " +
            "s.sellDate, s.sellPrice, s.customerName, " +
            "CAST(s.sellPrice - b.purchaseCost AS double)) " +
            "FROM Selling s JOIN s.buying b " +
            "WHERE FUNCTION('MONTH', s.sellDate) = :month AND FUNCTION('YEAR', s.sellDate) = :year")
    List<DashboardSellInfoDto> getSellingInfoWithProfitByMonth(@Param("month") int month, @Param("year") int year);

    @Query("SELECT COUNT(s) FROM Selling s WHERE FUNCTION('MONTH', s.sellDate) = :month AND FUNCTION('YEAR', s.sellDate) = :year")
    long countByMonthAndYear(@Param("month") int month, @Param("year") int year);
}
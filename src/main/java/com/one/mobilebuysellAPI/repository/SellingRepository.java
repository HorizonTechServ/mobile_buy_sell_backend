package com.one.mobilebuysellAPI.repository;

import com.one.mobilebuysellAPI.dto.DashboardSellInfoDto;
import com.one.mobilebuysellAPI.entity.Selling;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SellingRepository extends JpaRepository<Selling, Long> {


    @Query("SELECT new com.one.mobilebuysellAPI.dto.DashboardSellInfoDto(" +
            "b.modelNumber, b.storage, b.color, b.purchaseCost, " +
            "s.sellDate, s.sellPrice, s.customerName, " +
            "CAST(s.sellPrice - b.purchaseCost AS double)) " +
            "FROM Selling s JOIN s.buying b")
    List<DashboardSellInfoDto> getSellingInfoWithProfit();
}
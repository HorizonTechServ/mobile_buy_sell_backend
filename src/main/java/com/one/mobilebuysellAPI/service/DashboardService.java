package com.one.mobilebuysellAPI.service;

import com.one.mobilebuysellAPI.dto.BuyingDto;
import com.one.mobilebuysellAPI.dto.DashboardCountSummaryDto;
import com.one.mobilebuysellAPI.dto.DashboardSellInfoDto;
import com.one.mobilebuysellAPI.dto.SellingDto;
import java.util.List;

public interface DashboardService {
    List<BuyingDto> getAllBoughtProducts(Integer month, Integer year);
    BuyingDto getBoughtProductByImei(String imeiNumber);
    List<SellingDto> getAllSoldProducts(Integer month, Integer year);
    List<DashboardSellInfoDto> getSellInfoSummaryWithProfit(Integer month, Integer year);
    DashboardCountSummaryDto getBuySellCountSummary(Integer month, Integer year);
}
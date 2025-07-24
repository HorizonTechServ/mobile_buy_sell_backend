package com.one.mobilebuysellAPI.service;

import com.one.mobilebuysellAPI.dto.BuyingDto;
import com.one.mobilebuysellAPI.dto.DashboardSellInfoDto;
import com.one.mobilebuysellAPI.dto.SellingDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface DashboardService {
    List<BuyingDto> getAllBoughtProducts();
    BuyingDto getBoughtProductByImei(String imeiNumber);
    List<SellingDto> getAllSoldProducts();
    List<DashboardSellInfoDto> getSellInfoSummaryWithProfit();
}
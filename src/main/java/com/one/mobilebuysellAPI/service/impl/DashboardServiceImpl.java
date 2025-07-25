package com.one.mobilebuysellAPI.service.impl;

import com.one.mobilebuysellAPI.dto.BuyingDto;
import com.one.mobilebuysellAPI.dto.DashboardCountSummaryDto;
import com.one.mobilebuysellAPI.dto.DashboardSellInfoDto;
import com.one.mobilebuysellAPI.dto.SellingDto;
import com.one.mobilebuysellAPI.entity.Buying;
import com.one.mobilebuysellAPI.entity.Selling;
import com.one.mobilebuysellAPI.repository.BuyingRepository;
import com.one.mobilebuysellAPI.repository.SellingRepository;
import com.one.mobilebuysellAPI.service.DashboardService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    private final BuyingRepository buyingRepository;
    private final SellingRepository sellingRepository;

    public DashboardServiceImpl(BuyingRepository buyingRepository, SellingRepository sellingRepository) {
        this.buyingRepository = buyingRepository;
        this.sellingRepository = sellingRepository;
    }

    @Override
    public List<BuyingDto> getAllBoughtProducts(Integer month, Integer year) {
        List<Buying> buyings;

        if (month != null && year != null) {
            buyings = buyingRepository.findByMonthAndYear(month, year);
        } else {
            buyings = buyingRepository.findAll();
        }

        return buyings.stream().map(buying -> {
            BuyingDto dto = new BuyingDto();
            BeanUtils.copyProperties(buying, dto);
            return dto;
        }).collect(Collectors.toList());
    }


    @Override
    public BuyingDto getBoughtProductByImei(String imeiNumber) {
        Buying buying = buyingRepository.findByImeiNumber(imeiNumber)
                .orElseThrow(() -> new RuntimeException("IMEI not found"));
        BuyingDto dto = new BuyingDto();
        BeanUtils.copyProperties(buying, dto);
        return dto;
    }

    @Override
    public List<SellingDto> getAllSoldProducts(Integer month, Integer year) {

        List<Selling> sellings;

        if (month != null && year != null) {
            sellings = sellingRepository.findByMonthAndYear(month, year);
        } else {
            sellings = sellingRepository.findAll();
        }

        return sellings.stream().map(selling -> {
            SellingDto dto = new SellingDto();
            BeanUtils.copyProperties(selling, dto);
            if (selling.getBuying() != null) {
                dto.setImeiNumber(selling.getBuying().getImeiNumber());
            }
            return dto;
        }).collect(Collectors.toList());
    }


    @Override
    public List<DashboardSellInfoDto> getSellInfoSummaryWithProfit(Integer month, Integer year) {
        if (month != null && year != null) {
            return sellingRepository.getSellingInfoWithProfitByMonth(month, year);
        } else {
            return sellingRepository.getSellingInfoWithProfit();
        }
    }

    @Override
    public DashboardCountSummaryDto getBuySellCountSummary(Integer month, Integer year) {
        long buyingCount;
        long sellingCount;

        if (month != null && year != null) {
            buyingCount = buyingRepository.countByMonthAndYear(month, year);
            sellingCount = sellingRepository.countByMonthAndYear(month, year);
        } else {
            buyingCount = buyingRepository.count();
            sellingCount = sellingRepository.count();
        }

        return new DashboardCountSummaryDto(buyingCount, sellingCount);
    }
}
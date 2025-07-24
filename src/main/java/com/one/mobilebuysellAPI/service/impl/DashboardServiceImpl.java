package com.one.mobilebuysellAPI.service.impl;

import com.one.mobilebuysellAPI.dto.BuyingDto;
import com.one.mobilebuysellAPI.dto.DashboardSellInfoDto;
import com.one.mobilebuysellAPI.dto.SellingDto;
import com.one.mobilebuysellAPI.entity.Buying;
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
    public List<BuyingDto> getAllBoughtProducts() {
        return buyingRepository.findAll().stream().map(buying -> {
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
    public List<SellingDto> getAllSoldProducts() {
        return sellingRepository.findAll().stream().map(selling -> {
            SellingDto dto = new SellingDto();
            BeanUtils.copyProperties(selling, dto);
            if (selling.getBuying() != null) {
                dto.setImeiNumber(selling.getBuying().getImeiNumber());
            }
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<DashboardSellInfoDto> getSellInfoSummaryWithProfit() {
        return sellingRepository.getSellingInfoWithProfit();
    }

}
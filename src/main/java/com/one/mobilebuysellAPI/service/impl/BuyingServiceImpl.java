package com.one.mobilebuysellAPI.service.impl;

import com.one.mobilebuysellAPI.dto.BuyingDto;
import com.one.mobilebuysellAPI.entity.Buying;
import com.one.mobilebuysellAPI.repository.BuyingRepository;
import com.one.mobilebuysellAPI.service.BuyingService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BuyingServiceImpl implements BuyingService {

    private final BuyingRepository buyingRepository;

    public BuyingServiceImpl(BuyingRepository buyingRepository) {
        this.buyingRepository = buyingRepository;
    }

    @Override
    public BuyingDto addBuying(BuyingDto buyingDto) {
        Buying buying = new Buying();
        BeanUtils.copyProperties(buyingDto, buying);
        buying = buyingRepository.save(buying);
        BuyingDto result = new BuyingDto();
        BeanUtils.copyProperties(buying, result);
        return result;
    }

    @Override
    public List<BuyingDto> getAllBuyings() {
        return buyingRepository.findAll().stream().map(buying -> {
            BuyingDto dto = new BuyingDto();
            BeanUtils.copyProperties(buying, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public BuyingDto getByImeiNumber(String imeiNumber) {
        Buying buying = buyingRepository.findByImeiNumber(imeiNumber)
                .orElseThrow(() -> new RuntimeException("IMEI not found"));
        BuyingDto dto = new BuyingDto();
        BeanUtils.copyProperties(buying, dto);
        return dto;
    }
}
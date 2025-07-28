package com.one.mobilebuysellAPI.service;

import com.one.mobilebuysellAPI.dto.BuyingDto;

import java.util.List;

public interface BuyingService {
    BuyingDto addBuying(BuyingDto buyingDto);
    List<BuyingDto> getAllBuyings();
    BuyingDto getByImeiNumber(String imeiNumber);
    List<BuyingDto> getUnsoldPhones();
}
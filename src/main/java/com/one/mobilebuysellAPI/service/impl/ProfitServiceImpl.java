package com.one.mobilebuysellAPI.service.impl;

import com.one.mobilebuysellAPI.repository.SellingRepository;
import com.one.mobilebuysellAPI.service.ProfitService;
import org.springframework.stereotype.Service;

@Service
public class ProfitServiceImpl implements ProfitService {

    private final SellingRepository sellingRepository;

    public ProfitServiceImpl(SellingRepository sellingRepository) {
        this.sellingRepository = sellingRepository;
    }

    @Override
    public double getTotalProfit(Integer month, Integer year) {
        return sellingRepository.findAll().stream()
                .filter(sell -> {
                    if (sell.getSellDate() == null) return false;
                    if (month != null && sell.getSellDate().getMonthValue() != month) return false;
                    if (year != null && sell.getSellDate().getYear() != year) return false;
                    return true;
                })
                .mapToDouble(sell -> {
                    if (sell.getBuying() != null && sell.getSellPrice() != null && sell.getBuying().getPurchaseCost() != null) {
                        return sell.getSellPrice() - sell.getBuying().getPurchaseCost();
                    }
                    return 0.0;
                })
                .sum();
    }

}
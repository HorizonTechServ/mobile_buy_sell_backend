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
    public double getTotalProfit() {
        return sellingRepository.findAll().stream()
                .mapToDouble(sell -> {
                    if (sell.getBuying() != null && sell.getSellPrice() != null && sell.getBuying().getPurchaseCost() != null) {
                        return sell.getSellPrice() - sell.getBuying().getPurchaseCost();
                    }
                    return 0.0;
                })
                .sum();
    }
}
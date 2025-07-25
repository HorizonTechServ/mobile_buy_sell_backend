package com.one.mobilebuysellAPI.service.impl;

import com.one.mobilebuysellAPI.dto.SellingDto;
import com.one.mobilebuysellAPI.entity.Buying;
import com.one.mobilebuysellAPI.entity.Selling;
import com.one.mobilebuysellAPI.repository.BuyingRepository;
import com.one.mobilebuysellAPI.repository.SellingRepository;
import com.one.mobilebuysellAPI.service.SellingService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class SellingServiceImpl implements SellingService {

    private final SellingRepository sellingRepository;
    private final BuyingRepository buyingRepository;

    public SellingServiceImpl(SellingRepository sellingRepository, BuyingRepository buyingRepository) {
        this.sellingRepository = sellingRepository;
        this.buyingRepository = buyingRepository;
    }

    @Override
    public SellingDto addSelling(SellingDto sellingDto) {
        Buying buying = buyingRepository.findByImeiNumber(sellingDto.getImeiNumber())
                .orElseThrow(() -> new RuntimeException("IMEI not found in Buying records"));

        Selling selling = new Selling();
        BeanUtils.copyProperties(sellingDto, selling);
        selling.setBuying(buying);

        // Auto-generate unique invoice number
        String generatedInvoiceNumber = generateUniqueInvoiceNumber();
        selling.setInvoiceNumber(generatedInvoiceNumber);

        selling = sellingRepository.save(selling);

        SellingDto result = new SellingDto();
        BeanUtils.copyProperties(selling, result);
        result.setImeiNumber(selling.getBuying().getImeiNumber());
        return result;
    }


    @Override
    public List<SellingDto> getAllSellings() {
        return sellingRepository.findAll().stream().map(selling -> {
            SellingDto dto = new SellingDto();
            BeanUtils.copyProperties(selling, dto);
            if (selling.getBuying() != null) {
                dto.setImeiNumber(selling.getBuying().getImeiNumber());
            }
            return dto;
        }).collect(Collectors.toList());
    }


    private String generateUniqueInvoiceNumber() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomPart = String.format("%04d", new Random().nextInt(10000)); // 0000 to 9999
        String invoice = "INV-" + datePart + "-" + randomPart;

        // Check if invoice already exists — if so, regenerate (optional safety check)
        while (sellingRepository.existsByInvoiceNumber(invoice)) {
            randomPart = String.format("%04d", new Random().nextInt(10000));
            invoice = "INV-" + datePart + "-" + randomPart;
        }

        return invoice;
    }

}

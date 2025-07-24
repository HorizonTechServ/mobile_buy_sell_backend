package com.one.mobilebuysellAPI.service;
import com.one.mobilebuysellAPI.dto.SellingDto;

import java.util.List;

public interface SellingService {
    SellingDto addSelling(SellingDto sellingDto); // sellingDto.imeiNumber is used for lookup
    List<SellingDto> getAllSellings();
}
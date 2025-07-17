package com.one.arpitInstituteAPI.service;

import com.one.arpitInstituteAPI.entity.ReceiptConfig;

import java.util.List;

public interface ReceiptConfigService {
    ReceiptConfig create(ReceiptConfig config);
    ReceiptConfig update(Long id, ReceiptConfig config);
    List<ReceiptConfig> getAll();
}
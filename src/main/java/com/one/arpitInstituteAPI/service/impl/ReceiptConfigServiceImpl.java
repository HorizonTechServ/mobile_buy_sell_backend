package com.one.arpitInstituteAPI.service.impl;

import com.one.arpitInstituteAPI.entity.ReceiptConfig;
import com.one.arpitInstituteAPI.repository.ReceiptConfigRepository;
import com.one.arpitInstituteAPI.service.ReceiptConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReceiptConfigServiceImpl implements ReceiptConfigService {

    @Autowired
    private ReceiptConfigRepository configRepository;

    @Override
    public ReceiptConfig create(ReceiptConfig config) {
        if (!configRepository.findAll().isEmpty()) {
            throw new IllegalStateException("Configuration already exists");
        }
        return configRepository.save(config);
    }

    @Override
    public ReceiptConfig update(Long id, ReceiptConfig config) {
        Optional<ReceiptConfig> existing = configRepository.findById(id);
        if (existing.isEmpty()) throw new IllegalArgumentException("Config not found");

        ReceiptConfig current = existing.get();
        current.setTuitionFees(config.getTuitionFees());
        current.setStudentDevFees(config.getStudentDevFees());
        current.setLabLibSportsFees(config.getLabLibSportsFees());
        current.setOtherFees(config.getOtherFees());
        current.setReceiptPrefix(config.getReceiptPrefix());

        return configRepository.save(current);
    }

    @Override
    public List<ReceiptConfig> getAll() {
        return configRepository.findAll();
    }
}
package com.one.societyAPI.service.impl;

import com.one.societyAPI.entity.Society;
import com.one.societyAPI.repository.SocietyRepository;
import com.one.societyAPI.service.SocietyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SocietyServiceImpl implements SocietyService {

    @Autowired
    private SocietyRepository societyRepository;

    @Override
    public Society createSociety(Society society) {
        return societyRepository.save(society);
    }

    @Override
    public List<Society> getAllSocieties() {
        return societyRepository.findAll();
    }

    @Override
    public Society getSocietyById(Long id) {
        return societyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Society not found with ID: " + id));
    }

    @Override
    public Society updateSociety(Long id, Society updatedSociety) {
        Society existing = getSocietyById(id);
        existing.setName(updatedSociety.getName());
        existing.setAddress(updatedSociety.getAddress());
        existing.setCity(updatedSociety.getCity());
        existing.setState(updatedSociety.getState());
        existing.setPincode(updatedSociety.getPincode());
        return societyRepository.save(existing);
    }

    @Override
    public void deleteSociety(Long id) {
        if (!societyRepository.existsById(id)) {
            throw new RuntimeException("Society not found with ID: " + id);
        }
        societyRepository.deleteById(id);
    }
}
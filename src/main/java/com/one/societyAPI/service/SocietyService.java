package com.one.societyAPI.service;

import com.one.societyAPI.dto.CreateSocietyRequest;
import com.one.societyAPI.entity.Society;

import java.util.List;

public interface SocietyService {
    Society createSociety(CreateSocietyRequest request);
    List<Society> getAllSocieties();
    Society getSocietyById(Long id);
    void deleteSociety(Long id);
}
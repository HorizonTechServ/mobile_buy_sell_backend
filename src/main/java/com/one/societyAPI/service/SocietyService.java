package com.one.societyAPI.service;

import com.one.societyAPI.dto.CreateSocietyRequest;
import com.one.societyAPI.entity.Society;

import java.util.List;
import java.util.Map;

public interface SocietyService {
    Society createSociety(CreateSocietyRequest request);
    List<Society> getAllSocieties();
    Society getSocietyById(Long id);
    void deleteSociety(Long id);
    public Map<String, Object> getFlatsBySocietyId(Long societyId);
}
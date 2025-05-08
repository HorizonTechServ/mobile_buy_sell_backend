package com.one.societyAPI.service;

import com.one.societyAPI.entity.Society;

import java.util.List;

public interface SocietyService {

    Society createSociety(Society society);

    List<Society> getAllSocieties();

    Society getSocietyById(Long id);

    Society updateSociety(Long id, Society updatedSociety);

    void deleteSociety(Long id);
}
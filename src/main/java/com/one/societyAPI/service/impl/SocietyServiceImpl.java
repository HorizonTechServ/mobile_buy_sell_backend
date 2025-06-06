package com.one.societyAPI.service.impl;

import com.one.societyAPI.dto.CreateSocietyRequest;
import com.one.societyAPI.entity.Flat;
import com.one.societyAPI.entity.Society;
import com.one.societyAPI.exception.SocietyException;
import com.one.societyAPI.repository.FlatRepository;
import com.one.societyAPI.repository.SocietyRepository;
import com.one.societyAPI.service.SocietyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SocietyServiceImpl implements SocietyService {

    private final SocietyRepository societyRepository;

    private final FlatRepository flatRepository;


    @Autowired
    public SocietyServiceImpl(SocietyRepository societyRepository, FlatRepository flatRepository) {
        this.societyRepository = societyRepository;
        this.flatRepository = flatRepository;
    }

    @Override
    public Society createSociety(CreateSocietyRequest request) {
        Society society = new Society();
        society.setName(request.getName());
        society.setAddress(request.getAddress());
        society.setTotalFlats(request.getTotalFlats());

        List<Flat> flats = new ArrayList<>();
        if (request.getFlats() != null) {
            flats = request.getFlats().stream().map(flatRequest -> {
                Flat flat = new Flat();
                flat.setFlatNumber(flatRequest.getFlatNumber());
                flat.setBlock(flatRequest.getBlock());
                flat.setOwnerName(flatRequest.getOwnerName());
                flat.setSociety(society);
                return flat;
            }).collect(Collectors.toList());
        }
        society.setFlats(flats);

        return societyRepository.save(society);
    }


    @Override
    public List<Society> getAllSocieties() {
        return societyRepository.findAll();
    }

    @Override
    public Society getSocietyById(Long id) {
        return societyRepository.findById(id)
                .orElseThrow(() -> new SocietyException("Society not found with id: " + id));
    }

    @Override
    public void deleteSociety(Long id) {
        Society society = getSocietyById(id);
        societyRepository.delete(society);
    }

    public Map<String, Object> getFlatsBySocietyId(Long societyId) {
        List<Flat> available = flatRepository.findBySocietyIdAndUserIsNull(societyId);
        List<Flat> assigned = flatRepository.findBySocietyIdAndUserIsNotNull(societyId);

        int total = available.size() + assigned.size();

        Map<String, Object> result = new HashMap<>();
        result.put("totalFlats", total);
        result.put("availableFlats", available);
        result.put("assignedFlats", assigned);

        return result;
    }

    @Override
    public Society updateSociety(Long id, CreateSocietyRequest request) {
        Society existingSociety = societyRepository.findById(id)
                .orElseThrow(() -> new SocietyException("Society not found with id: " + id));

        existingSociety.setName(request.getName());
        existingSociety.setAddress(request.getAddress());
        existingSociety.setTotalFlats(request.getTotalFlats());

        if (request.getFlats() != null) {
            List<String> existingFlatNumbers = existingSociety.getFlats().stream()
                    .map(Flat::getFlatNumber)
                    .toList();

            for (var flatRequest : request.getFlats()) {
                if (!existingFlatNumbers.contains(flatRequest.getFlatNumber())) {
                    Flat flat = new Flat();
                    flat.setFlatNumber(flatRequest.getFlatNumber());
                    flat.setBlock(flatRequest.getBlock());
                    flat.setOwnerName(flatRequest.getOwnerName());
                    flat.setSociety(existingSociety);
                    existingSociety.getFlats().add(flat);
                }
            }
        }

        return societyRepository.save(existingSociety);
    }


}
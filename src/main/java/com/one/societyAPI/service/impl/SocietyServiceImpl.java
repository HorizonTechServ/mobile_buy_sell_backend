package com.one.societyAPI.service.impl;

import com.one.societyAPI.dto.CreateSocietyRequest;
import com.one.societyAPI.dto.FlatRequest;
import com.one.societyAPI.dto.PatchSocietyRequest;
import com.one.societyAPI.entity.Flat;
import com.one.societyAPI.entity.Society;
import com.one.societyAPI.exception.SocietyException;
import com.one.societyAPI.repository.FlatRepository;
import com.one.societyAPI.repository.SocietyRepository;
import com.one.societyAPI.service.SocietyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
    public Society patchSociety(Long id, PatchSocietyRequest request) {
        Society society = societyRepository.findById(id)
                .orElseThrow(() -> new SocietyException("Society not found with id: " + id));

        if (request.getName() != null) {
            society.setName(request.getName());
        }

        if (request.getAddress() != null) {
            society.setAddress(request.getAddress());
        }

        if (request.getTotalFlats() != null) {
            society.setTotalFlats(request.getTotalFlats());
        }

        if (request.getFlats() != null) {
            for (FlatRequest flatReq : request.getFlats()) {
                if (flatReq.getId() != null) {
                    // Try to update existing flat by ID
                    Flat existingFlat = society.getFlats().stream()
                            .filter(f -> f.getId().equals(flatReq.getId()))
                            .findFirst()
                            .orElseThrow(() -> new SocietyException("Flat not found with id: " + flatReq.getId()));

                    if (flatReq.getFlatNumber() != null) {
                        existingFlat.setFlatNumber(flatReq.getFlatNumber());
                    }
                    if (flatReq.getBlock() != null) {
                        existingFlat.setBlock(flatReq.getBlock());
                    }
                    if (flatReq.getOwnerName() != null) {
                        existingFlat.setOwnerName(flatReq.getOwnerName());
                    }
                } else {
                    // Add new flat
                    Flat newFlat = new Flat();
                    newFlat.setFlatNumber(flatReq.getFlatNumber());
                    newFlat.setBlock(flatReq.getBlock());
                    newFlat.setOwnerName(flatReq.getOwnerName());
                    newFlat.setSociety(society);
                    society.getFlats().add(newFlat);
                }
            }
        }

        return societyRepository.save(society);
    }


}
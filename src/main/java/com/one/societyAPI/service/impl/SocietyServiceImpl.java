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
import java.util.List;
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
}
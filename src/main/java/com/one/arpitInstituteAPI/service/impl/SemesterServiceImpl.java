package com.one.arpitInstituteAPI.service.impl;

import com.one.arpitInstituteAPI.entity.Semester;
import com.one.arpitInstituteAPI.repository.SemesterRepository;
import com.one.arpitInstituteAPI.service.SemesterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SemesterServiceImpl implements SemesterService {

    @Autowired
    private SemesterRepository repository;

    @Override
    public Semester create(Semester semester) {
        if (repository.existsByName(semester.getName())) {
            throw new IllegalArgumentException("Semester already exists");
        }
        return repository.save(semester);
    }

    @Override
    public List<Semester> getAll() {
        return repository.findAll();
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
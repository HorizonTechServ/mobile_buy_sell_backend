package com.one.arpitInstituteAPI.service.impl;

import com.one.arpitInstituteAPI.entity.Branch;
import com.one.arpitInstituteAPI.repository.BranchRepository;
import com.one.arpitInstituteAPI.service.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BranchServiceImpl implements BranchService {

    @Autowired
    private BranchRepository repository;

    @Override
    public Branch create(Branch branch) {
        if (repository.existsByName(branch.getName())) {
            throw new IllegalArgumentException("Branch already exists");
        }
        return repository.save(branch);
    }

    @Override
    public List<Branch> getAll() {
        return repository.findAll();
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
package com.one.arpitInstituteAPI.service.impl;

import com.one.arpitInstituteAPI.entity.Department;
import com.one.arpitInstituteAPI.repository.DepartmentRepository;
import com.one.arpitInstituteAPI.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentRepository repository;

    @Override
    public Department create(Department department) {
        if (repository.existsByName((department.getName()))) {
            throw new IllegalArgumentException("Department already exists");
        }
        return repository.save(department);
    }

    @Override
    public List<Department> getAll() {
        return repository.findAll();
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
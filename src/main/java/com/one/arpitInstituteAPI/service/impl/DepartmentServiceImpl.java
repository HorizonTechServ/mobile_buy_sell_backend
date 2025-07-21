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
    public Department getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Department not found with ID: " + id));
    }

    @Override
    public Department update(Long id, Department updated) {
        Department existing = getById(id);
        existing.setName(updated.getName());
        return repository.save(existing);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
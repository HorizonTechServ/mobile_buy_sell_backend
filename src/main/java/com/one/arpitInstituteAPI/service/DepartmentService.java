package com.one.arpitInstituteAPI.service;

import com.one.arpitInstituteAPI.entity.Department;

import java.util.List;

public interface DepartmentService {
    Department create(Department department);
    List<Department> getAll();
    void delete(Long id);
}
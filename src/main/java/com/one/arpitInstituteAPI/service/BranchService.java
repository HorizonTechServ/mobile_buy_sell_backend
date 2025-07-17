package com.one.arpitInstituteAPI.service;

import com.one.arpitInstituteAPI.entity.Branch;

import java.util.List;

public interface BranchService {
    Branch create(Branch branch);
    List<Branch> getAll();
    void delete(Long id);
}
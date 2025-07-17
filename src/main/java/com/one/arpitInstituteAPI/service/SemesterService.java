package com.one.arpitInstituteAPI.service;

import com.one.arpitInstituteAPI.entity.Semester;
import java.util.List;

public interface SemesterService {
    Semester create(Semester semester);
    List<Semester> getAll();
    void delete(Long id);
}
package com.one.arpitInstituteAPI.repository;

import com.one.arpitInstituteAPI.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByStudentName(String studentName);
}
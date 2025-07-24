package com.one.arpitInstituteAPI.repository;

import com.one.arpitInstituteAPI.dto.StudentNameDTO;
import com.one.arpitInstituteAPI.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByStudentName(String studentName);

    Page<Student> findAll(Pageable pageable);

    @Query("SELECT new com.one.arpitInstituteAPI.dto.StudentNameDTO(s.studentName) FROM Student s")
    List<StudentNameDTO> findAllStudentNames();
}
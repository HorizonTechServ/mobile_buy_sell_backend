package com.one.societyAPI.repository;

import com.one.societyAPI.entity.Flat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlatRepository extends JpaRepository<Flat, Long> {

    List<Flat> findBySocietyIdAndUserIsNull(Long societyId);

    List<Flat> findBySocietyIdAndUserIsNotNull(Long societyId);
}

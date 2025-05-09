package com.one.societyAPI.repository;

import com.one.societyAPI.entity.Society;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocietyRepository extends JpaRepository<Society, Long> {
}
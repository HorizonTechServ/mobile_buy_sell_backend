package com.one.societyAPI.repository;

import com.one.societyAPI.entity.Society;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocietyRepository extends JpaRepository<Society, Long> {
}
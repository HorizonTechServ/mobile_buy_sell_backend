package com.one.mobilebuysellAPI.repository;

import com.one.mobilebuysellAPI.entity.Buying;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BuyingRepository extends JpaRepository<Buying, Long> {

    @Query("SELECT b FROM Buying b WHERE FUNCTION('MONTH', b.purchaseDate) = :month AND FUNCTION('YEAR', b.purchaseDate) = :year")
    List<Buying> findByMonthAndYear(@Param("month") int month, @Param("year") int year);

    Optional<Buying> findByImeiNumber(String imeiNumber);

    @Query("SELECT COUNT(b) FROM Buying b WHERE FUNCTION('MONTH', b.purchaseDate) = :month AND FUNCTION('YEAR', b.purchaseDate) = :year")
    long countByMonthAndYear(@Param("month") int month, @Param("year") int year);

    List<Buying> findBySoldStatusIsNullOrSoldStatusNotIgnoreCase(String soldStatus);

}
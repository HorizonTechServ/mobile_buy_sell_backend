package com.one.mobilebuysellAPI.repository;

import com.one.mobilebuysellAPI.entity.Buying;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BuyingRepository extends JpaRepository<Buying, Long> {
    Optional<Buying> findByImeiNumber(String imeiNumber);

    // Example: filter by modelNumber and color with pagination
    Page<Buying> findByModelNumberContainingAndColorContaining(
            String modelNumber, String color, Pageable pageable
    );
}
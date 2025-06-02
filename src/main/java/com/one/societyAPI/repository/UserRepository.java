package com.one.societyAPI.repository;

import com.one.societyAPI.entity.User;
import com.one.societyAPI.utils.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByMobileNumber(String mobileNumber);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);

    boolean existsByMobileNumber(String contact);

    // Get users by society ID and role
    List<User> findByFlat_Society_IdAndRole(Long societyId, UserRole role);

    // Get admin by society ID
    Optional<User> findBySociety_IdAndRole(Long societyId, UserRole role);

    List<User> findByFlat_Society_Id(Long id);

    List<User> findBySocietyIdAndRole(Long societyId, UserRole role);

}

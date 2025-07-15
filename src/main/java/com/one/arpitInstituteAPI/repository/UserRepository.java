package com.one.arpitInstituteAPI.repository;

import com.one.arpitInstituteAPI.entity.User;
import com.one.arpitInstituteAPI.utils.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
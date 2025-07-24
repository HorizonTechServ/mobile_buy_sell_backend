package com.one.mobilebuysellAPI.repository;

import com.one.mobilebuysellAPI.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUsername(String username);

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
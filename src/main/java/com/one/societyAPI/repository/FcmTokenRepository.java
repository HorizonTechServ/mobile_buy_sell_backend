package com.one.societyAPI.repository;

import com.one.societyAPI.entity.FcmToken;
import com.one.societyAPI.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    List<FcmToken> findByUserId(Long userId);
    List<FcmToken> findByUserIn(List<User> users);

    Optional<FcmToken> findByToken(String fcmToken);
}
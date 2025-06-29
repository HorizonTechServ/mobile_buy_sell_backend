package com.one.societyAPI.service;

import com.one.societyAPI.entity.FcmToken;
import com.one.societyAPI.entity.User;
import com.one.societyAPI.repository.FcmTokenRepository;
import com.one.societyAPI.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FcmTokenService {

    private final FcmTokenRepository fcmTokenRepository;
    private final UserRepository userRepository;

    public FcmTokenService(FcmTokenRepository fcmTokenRepository, UserRepository userRepository) {
        this.fcmTokenRepository = fcmTokenRepository;
        this.userRepository = userRepository;
    }

    public void registerFcmToken(Long userId, String fcmToken) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<FcmToken> existing = fcmTokenRepository.findByToken(fcmToken);

        // If token already exists, skip inserting
        if (existing.isEmpty()) {
            FcmToken token = new FcmToken();
            token.setToken(fcmToken);
            token.setUser(user);
            fcmTokenRepository.save(token);
        }
          // Optional: if token exists but belongs to another user, update owner
        else if (!existing.get().getUser().getId().equals(userId)) {
            FcmToken token = existing.get();
            token.setUser(user);
            fcmTokenRepository.save(token);
        }
    }
}


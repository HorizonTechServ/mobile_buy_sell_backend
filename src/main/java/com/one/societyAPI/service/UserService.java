package com.one.societyAPI.service;
import com.one.societyAPI.entity.User;
import com.one.societyAPI.exception.UserException;
import com.one.societyAPI.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean isUserIdUnique(String mobileNumber) {
        return userRepository.findByMobileNumber(mobileNumber).isEmpty();
    }

    public User registerUser(User user) {
        // Check if user already exists

        Optional<User> existingUser = userRepository.findByMobileNumber(user.getMobileNumber());

        if (existingUser.isPresent()) {
            throw new UserException("User is already exists!");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email is already taken");
        }

        user.setLastLogin(LocalDateTime.now());

        // 🔹 Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public User updateUser(User updatedUserData) {

        User existingUser = userRepository.findById(updatedUserData.getId())
                .orElseThrow(() -> new UserException("User not found"));

        // Optional: avoid overwriting sensitive data unless explicitly allowed
        if (updatedUserData.getEmail() != null) {
            if (!existingUser.getEmail().equals(updatedUserData.getEmail())
                    && userRepository.existsByEmail(updatedUserData.getEmail())) {
                throw new UserException("Email already in use");
            }
            existingUser.setEmail(updatedUserData.getEmail());
        }

        if (!existingUser.getMobileNumber().equals(updatedUserData.getMobileNumber())
                && userRepository.existsByMobileNumber(updatedUserData.getMobileNumber())) {
            throw new UserException("Contact already in use");
        }
        existingUser.setMobileNumber(updatedUserData.getMobileNumber());

        if (updatedUserData.getName() != null) {
            existingUser.setName(updatedUserData.getName());
        }

        if (updatedUserData.getGender() != null) {
            existingUser.setGender(updatedUserData.getGender());
        }

        return userRepository.save(existingUser);
    }
}
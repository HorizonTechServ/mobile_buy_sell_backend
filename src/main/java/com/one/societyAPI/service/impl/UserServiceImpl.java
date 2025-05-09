package com.one.societyAPI.service.impl;

import com.one.societyAPI.dto.UserDTO;
import com.one.societyAPI.entity.User;
import com.one.societyAPI.exception.UserException;
import com.one.societyAPI.repository.UserRepository;
import com.one.societyAPI.service.UserService;
import com.one.societyAPI.utils.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean isUserIdUnique(String mobileNumber) {
        return userRepository.findByMobileNumber(mobileNumber).isEmpty();
    }

    @Override
    public UserDTO registerSuperAdmin(User user) {
        validateUser(user);
        return toDTO(userRepository.save(prepareUser(user, UserRole.SUPER_ADMIN)));
    }

    @Override
    public UserDTO registerAdmin(User user) {
        validateUser(user);
        return toDTO(userRepository.save(prepareUser(user, UserRole.ADMIN)));
    }

    @Override
    public UserDTO registerUser(User user) {
        validateUser(user);
        return toDTO(userRepository.save(prepareUser(user, UserRole.USER)));
    }

    @Override
    public UserDTO updateUser(User updatedUserData) {
        User existingUser = userRepository.findById(updatedUserData.getId())
                .orElseThrow(() -> new UserException("User not found"));

        if (updatedUserData.getEmail() != null &&
                !existingUser.getEmail().equals(updatedUserData.getEmail()) &&
                userRepository.existsByEmail(updatedUserData.getEmail())) {
            throw new UserException("Email already in use");
        }
        if (updatedUserData.getEmail() != null) {
            existingUser.setEmail(updatedUserData.getEmail());
        }

        if (updatedUserData.getMobileNumber() != null &&
                !existingUser.getMobileNumber().equals(updatedUserData.getMobileNumber()) &&
                userRepository.existsByMobileNumber(updatedUserData.getMobileNumber())) {
            throw new UserException("Contact already in use");
        }
        if (updatedUserData.getMobileNumber() != null) {
            existingUser.setMobileNumber(updatedUserData.getMobileNumber());
        }

        if (updatedUserData.getName() != null) {
            existingUser.setName(updatedUserData.getName());
        }

        if (updatedUserData.getGender() != null) {
            existingUser.setGender(updatedUserData.getGender());
        }

        return toDTO(userRepository.save(existingUser));
    }

    // ---------- Private Utility Methods ----------

    private void validateUser(User user) {
        if (userRepository.findByMobileNumber(user.getMobileNumber()).isPresent()) {
            throw new UserException("User with this mobile number already exists!");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserException("Email is already taken");
        }
    }

    private User prepareUser(User user, UserRole role) {
        user.setRole(role);
        user.setLastLogin(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return user;
    }

    private UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getMobileNumber(),
                user.getGender(),
                user.getRole(),
                user.getStatus(),
                user.getLastLogin(),
                user.getCreatedAt()
        );
    }
}
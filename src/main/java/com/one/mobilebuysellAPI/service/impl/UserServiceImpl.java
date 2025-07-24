package com.one.mobilebuysellAPI.service.impl;

import com.one.mobilebuysellAPI.dto.UserDTO;
import com.one.mobilebuysellAPI.entity.User;
import com.one.mobilebuysellAPI.exception.UserException;
import com.one.mobilebuysellAPI.repository.UserRepository;
import com.one.mobilebuysellAPI.service.UserService;
import com.one.mobilebuysellAPI.utils.UserRole;
import com.one.mobilebuysellAPI.utils.UserStatus;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,  PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean isUserIdUnique(String username) {
        return userRepository.findUserByUsername(username).isEmpty();
    }



    @Override
    @Transactional
    public UserDTO registerAdmin(User user) {
        validateUser(user);

        user.setAdmin(false); // Ensure all users are not super admins by default

        user.setRole(UserRole.ADMIN);

        return toDTO(userRepository.save(user));
    }



    @Override
    public UserDTO getUserDetailsById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User not found with ID: " + userId));

        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getMobileNumber(),
                user.getUsername(),
                user.getGender(),
                user.getRole(),
                user.getStatus(),
                user.getLastLogin(),
                user.getCreatedAt()
        );
    }


    @Override
    @Transactional
    public void softDeleteUserById(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User not found with ID: " + userId));

        if (user.getStatus() == UserStatus.DELETE) {
            throw new UserException("User is already deleted.");
        }

        user.setStatus(UserStatus.DELETE);

        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserDTO editAdmin(Long userId, Map<String, Object> updates) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User not found with user id: " + userId));

        if (updates.containsKey("mobileNumber")) {
            user.setMobileNumber((String) updates.get("mobileNumber"));
        }

        if (updates.containsKey("username")) {
            String newUsername = (String) updates.get("username");
            if (!newUsername.equals(user.getUsername()) &&
                    userRepository.findUserByUsername(newUsername).isPresent()) {
                throw new UserException(newUsername + " Username already in use");
            }
            user.setUsername(newUsername);
        }

        if (updates.containsKey("email")) {
            String newEmail = (String   ) updates.get("email");
            if (!newEmail.equalsIgnoreCase(user.getEmail()) &&
                    userRepository.findByEmail(newEmail).isPresent()) {
                throw new UserException(newEmail + " email is already in use");
            }
            user.setEmail(newEmail);
        }

        // Add gender field update
        if (updates.containsKey("gender")) {
            user.setGender((String) updates.get("gender"));
        }

        // Add this block to update password
        if (updates.containsKey("password")) {
            String rawPassword = (String) updates.get("password");
            String encodedPassword = passwordEncoder.encode(rawPassword);
            user.setPassword(encodedPassword);
        }
        return toDTO(userRepository.save(user));
    }


    @Override
    public void updateUserProfilePicture(Long userId, MultipartFile file) throws IOException {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new UserException("User not found");
        }

        User user = userOpt.get();
        user.setProfilePicture(file.getBytes());
        userRepository.save(user);
    }


    // ---------- Private Utility Methods ----------
    private void validateUser(User user) {
        if (userRepository.findUserByUsername(user.getUsername()).isPresent()) {
            throw new UserException(user.getUsername() + " User with this name already exists!");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserException(user.getEmail() + " Email is already taken");
        }
    }

    private UserDTO toDTO(User user) {

        return new UserDTO(
                user.getId(),
                user.getEmail(),
                user.getMobileNumber(),
                user.getUsername(),
                user.getGender(),
                user.getRole(),
                user.getStatus(),
                user.getLastLogin(),
                user.getCreatedAt()
        );
    }

}
package com.one.digitalapi.service;
import com.one.digitalapi.entity.User;
import com.one.digitalapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public boolean isUserIdUnique(String userId) {
        return userRepository.findByUserId(userId).isEmpty();
    }

    public User registerUser(User user) {
        // Check if user already exists

        Optional<User> existingUser = userRepository.findByUserId(user.getUserId());

        if (existingUser.isPresent()) {
            throw new RuntimeException("User ID already exists!");
        }

        // 🔹 Hash the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

}
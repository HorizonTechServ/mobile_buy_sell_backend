package com.one.societyAPI.service.impl;

import com.one.societyAPI.dto.UserDTO;
import com.one.societyAPI.entity.Flat;
import com.one.societyAPI.entity.Society;
import com.one.societyAPI.entity.User;
import com.one.societyAPI.exception.UserException;
import com.one.societyAPI.repository.FlatRepository;
import com.one.societyAPI.repository.SocietyRepository;
import com.one.societyAPI.repository.UserRepository;
import com.one.societyAPI.service.UserService;
import com.one.societyAPI.utils.UserRole;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SocietyRepository societyRepository;
    private final FlatRepository flatRepository;


    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, SocietyRepository societyRepository, FlatRepository flatRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.societyRepository = societyRepository;
        this.flatRepository = flatRepository;
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
    @Transactional
    public UserDTO registerAdmin(User user, Long societyId) {
        validateUser(user);

        Society society = societyRepository.findById(societyId)
                .orElseThrow(() -> new RuntimeException("Society not found"));

        user.setRole(UserRole.ADMIN);
        user.setSociety(society); // Assign society to admin

        return toDTO(userRepository.save(user));
    }

    @Override
    public UserDTO registerUser(User user, Long flatId) {

        validateUser(user);

        Flat flat = flatRepository.findById(flatId)
                .orElseThrow(() -> new RuntimeException("Flat not found"));

        if (flat.getUser() != null) {
            throw new RuntimeException("Flat already assigned to another user");
        }
        user.setRole(UserRole.USER);

        user.setFlat(flat); // Assign flat to user

        return toDTO(userRepository.save(user));
    }

    @Override
    public List<UserDTO> getUsersBySocietyId(Long societyId) {
        List<User> users = userRepository.findByFlat_Society_IdAndRole(societyId, UserRole.USER);
        return users.stream().map(this::toDTO).toList();    }


    @Override
    public UserDTO getAdminBySocietyId(Long societyId) {
        User admin = userRepository.findBySociety_IdAndRole(societyId, UserRole.ADMIN)
                .orElseThrow(() -> new UserException("No admin found for this society"));
        return toDTO(admin);
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
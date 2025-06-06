package com.one.societyAPI.service.impl;

import com.one.societyAPI.dto.UserDTO;
import com.one.societyAPI.entity.Flat;
import com.one.societyAPI.entity.MaintenancePayment;
import com.one.societyAPI.entity.Society;
import com.one.societyAPI.entity.User;
import com.one.societyAPI.exception.FlatException;
import com.one.societyAPI.exception.SocietyException;
import com.one.societyAPI.exception.UserException;
import com.one.societyAPI.repository.FlatRepository;
import com.one.societyAPI.repository.MaintenancePaymentRepository;
import com.one.societyAPI.repository.SocietyRepository;
import com.one.societyAPI.repository.UserRepository;
import com.one.societyAPI.service.UserService;
import com.one.societyAPI.utils.PaymentStatus;
import com.one.societyAPI.utils.UserRole;
import com.one.societyAPI.utils.UserStatus;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SocietyRepository societyRepository;
    private final FlatRepository flatRepository;
    private final MaintenancePaymentRepository paymentRepository;



    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, SocietyRepository societyRepository, FlatRepository flatRepository, MaintenancePaymentRepository paymentRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.societyRepository = societyRepository;
        this.flatRepository = flatRepository;
        this.paymentRepository = paymentRepository;
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
                .orElseThrow(() -> new SocietyException("Society not found with id: " + societyId));

        user.setRole(UserRole.ADMIN);
        user.setSociety(society); // Assign society to admin

        return toDTO(userRepository.save(user));
    }

    @Override
    public UserDTO registerUser(User user, Long flatId, Long societyId) {

        validateUser(user);

        Flat flat = flatRepository.findById(flatId)
                .orElseThrow(() -> new FlatException("Flat not found with id: " + flatId));

        Society society = societyRepository.findById(societyId)
                .orElseThrow(() -> new SocietyException("Society not found with id: " + societyId));

        if (flat.getUser() != null) {
            throw new FlatException("Flat already assigned to another user");
        }
        user.setRole(UserRole.USER);

        user.setFlat(flat); // Assign flat to user
        user.setSociety(society); // Assign society to user

        return toDTO(userRepository.save(user));
    }

    @Override
    public List<UserDTO> getUsersBySocietyId(Long societyId) {
        List<User> users = userRepository.findByFlat_Society_IdAndRole(societyId, UserRole.USER);
        return users.stream().map(this::toDTO).toList();    }


    @Override
    public List<UserDTO> getAdminsBySocietyId(Long societyId) {
        List<User> admins = userRepository.findBySociety_IdAndRole(societyId, UserRole.ADMIN);

        if (admins.isEmpty()) {
            throw new UserException("No admin found for this society");
        }

        return admins.stream().map(this::toDTO).collect(Collectors.toList());
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

        // Unassign flat if assigned
        Flat assignedFlat = user.getFlat();
        if (assignedFlat != null) {
            user.setFlat(null);        // Remove flat from user
            assignedFlat.setUser(null); // Optional: remove user from flat if bi-directional
            flatRepository.save(assignedFlat);
        }

        userRepository.save(user);
    }

    @Override
    public List<UserDTO> getUsersBySocietyIdAndRoleUser(Long societyId) {
        List<User> users = userRepository.findBySocietyIdAndRole(societyId, UserRole.USER);
        return users.stream().map(this::toDTO).collect(Collectors.toList());
    }



    @Override
    @Transactional
    public UserDTO editUser(Long userId, Map<String, Object> updates) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User not found with user id: " + userId));

        if (updates.containsKey("name")) {
            user.setName((String) updates.get("name"));
        }

        if (updates.containsKey("mobileNumber")) {
            String newMobile = (String) updates.get("mobileNumber");
            if (!newMobile.equals(user.getMobileNumber()) &&
                    userRepository.findByMobileNumber(newMobile).isPresent()) {
                throw new UserException(newMobile + " Mobile number already in use");
            }
            user.setMobileNumber(newMobile);
        }

        if (updates.containsKey("email")) {
            String newEmail = (String) updates.get("email");
            if (!newEmail.equalsIgnoreCase(user.getEmail()) &&
                    userRepository.findByEmail(newEmail).isPresent()) {
                throw new UserException(newEmail + " email is already in use");
            }
            user.setEmail(newEmail);
        }

        if (updates.containsKey("flatId")) {
            Long flatId = Long.valueOf(updates.get("flatId").toString());
            Flat flat = flatRepository.findById(flatId)
                    .orElseThrow(() -> new UserException("Flat not found with id " + flatId));

            if (flat.getUser() != null && !flat.getUser().getId().equals(user.getId())) {
                throw new UserException("Flat already assigned to another user");
            }

            user.setFlat(flat);
        }

        return toDTO(userRepository.save(user));
    }



    // ---------- Private Utility Methods ----------
    private void validateUser(User user) {
        if (userRepository.findByMobileNumber(user.getMobileNumber()).isPresent()) {
            throw new UserException(user.getMobileNumber() + " User with this mobile number already exists!");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserException(user.getEmail() + " Email is already taken");
        }
    }

    private User prepareUser(User user, UserRole role) {
        user.setRole(role);
        user.setLastLogin(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return user;
    }

    private UserDTO toDTO(User user) {
        // Fetch payments for this user
        List<MaintenancePayment> payments = paymentRepository.findByUser(user);

        // Determine maintenance status
        boolean hasPending = payments.stream()
                .anyMatch(p -> p.getStatus() == PaymentStatus.PENDING);

        String maintenanceStatus = hasPending ? "PENDING" : "PAID";

        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getMobileNumber(),
                user.getGender(),
                user.getRole(),
                user.getStatus(),
                user.getLastLogin(),
                user.getCreatedAt(),
                maintenanceStatus // <- add this here
        );
    }
}
package com.one.societyAPI.controller;

import com.one.societyAPI.dto.UserDTO;
import com.one.societyAPI.entity.User;
import com.one.societyAPI.exception.UserException;
import com.one.societyAPI.logger.DefaultLogger;
import com.one.societyAPI.repository.UserRepository;
import com.one.societyAPI.service.UserService;
import com.one.societyAPI.utils.UserStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.one.societyAPI.exception.GlobalExceptionHandler.getMapResponseEntity;

@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "APIs for managing super admin, admin and users")
public class UserController {

    private static final String CLASSNAME = "UserController";
    private static final DefaultLogger LOGGER = new DefaultLogger(UserController.class);

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/register/superAdmin")
    @Operation(summary = "Register a Super Admin", description = "Creates a new super admin with email and password")
    public ResponseEntity<?> registerSuperAdmin(@Valid @RequestBody User user) {
        String methodName = "registerSuperAdmin";
        LOGGER.infoLog(CLASSNAME, methodName, "Received request to register a super admin: " + user);

        try {
            UserDTO savedUser = userService.registerSuperAdmin(user);
            LOGGER.infoLog(CLASSNAME, methodName, "Super Admin registered successfully: " + savedUser);
            return ResponseEntity.ok(savedUser);
        } catch (UserException e) {
            LOGGER.errorLog(CLASSNAME, methodName, "Error registering super admin: " + e.getMessage());
            return getMapResponseEntity(e.getMessage(), e);
        }
    }

    @PostMapping("/register/admin")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Register an Admin", description = "Creates a new admin")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody User user) {
        String methodName = "registerAdmin";
        LOGGER.infoLog(CLASSNAME, methodName, "Received request to register an admin: " + user);

        try {
            UserDTO savedUser = userService.registerAdmin(user);
            LOGGER.infoLog(CLASSNAME, methodName, "Admin registered successfully: " + savedUser);
            return ResponseEntity.ok(savedUser);
        } catch (UserException e) {
            LOGGER.errorLog(CLASSNAME, methodName, "Error registering admin: " + e.getMessage());
            return getMapResponseEntity(e.getMessage(), e);
        }
    }

    @PostMapping("/register/user")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Register a User", description = "Creates a new user")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        String methodName = "registerUser";
        LOGGER.infoLog(CLASSNAME, methodName, "Received request to register a user: " + user);

        try {
            UserDTO savedUser = userService.registerUser(user);
            LOGGER.infoLog(CLASSNAME, methodName, "User registered successfully: " + savedUser);
            return ResponseEntity.ok(savedUser);
        } catch (UserException e) {
            LOGGER.errorLog(CLASSNAME, methodName, "Error registering user: " + e.getMessage());
            return getMapResponseEntity(e.getMessage(), e);
        }
    }

    @PatchMapping("/update")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Update user profile", description = "Partially updates the user profile")
    public ResponseEntity<?> updateUserProfile(@RequestBody User updatedFields) {
        String methodName = "updateUserProfile";
        LOGGER.infoLog(CLASSNAME, methodName, "Received request to update user: " + updatedFields);

        try {
            UserDTO updatedUser = userService.updateUser(updatedFields);
            LOGGER.infoLog(CLASSNAME, methodName, "User updated successfully: " + updatedUser);
            return ResponseEntity.ok(updatedUser);
        } catch (UserException e) {
            LOGGER.errorLog(CLASSNAME, methodName, "Error updating user: " + e.getMessage());
            return getMapResponseEntity(e.getMessage(), e);
        }
    }

    @PutMapping("/delete/{mobileNumber}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Soft delete user", description = "Marks user status as DELETED based on mobile number")
    public ResponseEntity<?> deleteUserAccount(@PathVariable String mobileNumber) {
        String methodName = "deleteUserAccount";
        LOGGER.infoLog(CLASSNAME, methodName, "Deleting user with mobile: " + mobileNumber);

        Optional<User> userOpt = userRepository.findByMobileNumber(mobileNumber);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getStatus() == UserStatus.ACTIVE) {
                user.setStatus(UserStatus.DELETE);
                userRepository.save(user);
                LOGGER.infoLog(CLASSNAME, methodName, "User marked as deleted.");
                Map<String, String> response = new HashMap<>();
                response.put("message", "User marked as DELETED.");
                return ResponseEntity.ok(response);
            } else {
                LOGGER.warnLog(CLASSNAME, methodName, "User not active.");
                return ResponseEntity.badRequest().body(Map.of("error", "User is not ACTIVE."));
            }
        } else {
            LOGGER.warnLog(CLASSNAME, methodName, "User not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found."));
        }
    }

    @GetMapping("/{mobileNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Get user", description = "Fetch user by mobile number")
    public ResponseEntity<?> getUserByMobileNumber(@PathVariable String mobileNumber) {
        String methodName = "getUserByMobileNumber";
        LOGGER.infoLog(CLASSNAME, methodName, "Fetching user with mobile: " + mobileNumber);

        Optional<User> userOpt = userRepository.findByMobileNumber(mobileNumber);
        if (userOpt.isPresent()) {
            return ResponseEntity.ok(userOpt.get());
        } else {
            LOGGER.warnLog(CLASSNAME, methodName, "User not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "User not found."));
        }
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<Map<String, Object>> handleUserException(UserException ex) {
        String methodName = "handleUserException";
        LOGGER.errorLog(CLASSNAME, methodName, "UserException occurred: " + ex.getMessage());
        return getMapResponseEntity(ex.getMessage(), ex);
    }
}
package com.one.societyAPI.controller;

import com.one.societyAPI.dto.AdminRegisterRequest;
import com.one.societyAPI.dto.UserDTO;
import com.one.societyAPI.dto.UserRegisterRequest;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

import static com.one.societyAPI.exception.GlobalExceptionHandler.getMapResponseEntity;

@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "APIs for managing super admin, admin and users")
public class UserController {

    private static final String CLASSNAME = "UserController";
    private static final DefaultLogger LOGGER = new DefaultLogger(UserController.class);

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register/superAdmin")
    @Operation(summary = "Register a Super Admin", description = "Creates a new super admin with email and password")
    public ResponseEntity<?> registerSuperAdmin(@Valid @RequestBody User user) {
        String methodName = "registerSuperAdmin";
        LOGGER.infoLog(CLASSNAME, methodName, "Received request to register a super admin: {}" + user);

        try {
            UserDTO savedUser = userService.registerSuperAdmin(user);
            LOGGER.infoLog(CLASSNAME, methodName, "Super Admin registered successfully: {}" + savedUser);
            return ResponseEntity.ok(savedUser);
        } catch (UserException e) {
            LOGGER.errorLog(CLASSNAME, methodName, "Error registering super admin: {}" + e.getMessage());
            return getMapResponseEntity(e.getMessage(), e);
        }
    }

    @PostMapping("/register-admin")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Register an Admin", description = "Creates a new admin")
    public ResponseEntity<UserDTO> registerAdmin(@RequestBody AdminRegisterRequest request) {
        String method = "registerAdmin";
        LOGGER.infoLog(CLASSNAME, method, "Received request to register admin: {}" + request);

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setMobileNumber(request.getMobileNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setGender(request.getGender());
        user.setLastLogin(LocalDateTime.now());

        UserDTO savedAdmin = userService.registerAdmin(user, request.getSocietyId());

        LOGGER.infoLog(CLASSNAME, method, "Admin registered successfully: {}" + savedAdmin);
        return ResponseEntity.ok(savedAdmin);
    }

    @PostMapping("/register/user")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Register a User", description = "Creates a new user")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterRequest request) {
        String method = "registerUser";
        LOGGER.infoLog(CLASSNAME, method, "Received request to register user: {}" + request);

        try {
            User user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setMobileNumber(request.getMobileNumber());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(request.getRole());
            user.setGender(request.getGender());
            user.setLastLogin(LocalDateTime.now());

            UserDTO savedUser = userService.registerUser(user, request.getFlatId());
            LOGGER.infoLog(CLASSNAME, method, "User registered successfully: {}" + savedUser);
            return ResponseEntity.ok(savedUser);
        } catch (UserException e) {
            LOGGER.errorLog(CLASSNAME, method, "Error registering user: {}" + e.getMessage());
            return getMapResponseEntity(e.getMessage(), e);
        }
    }


    @GetMapping("/by-society/{societyId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Get Users by Society ID", description = "Fetch all users in a specific society")
    public ResponseEntity<?> getUsersBySociety(@PathVariable Long societyId) {
        try {
            return ResponseEntity.ok(userService.getUsersBySocietyId(societyId));
        } catch (Exception e) {
            return getMapResponseEntity("Error fetching users", e);
        }
    }

    @GetMapping("/admin/by-society/{societyId}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Get Admin by Society ID", description = "Fetch admin assigned to a specific society")
    public ResponseEntity<?> getAdminBySociety(@PathVariable Long societyId) {
        try {
            return ResponseEntity.ok(userService.getAdminBySocietyId(societyId));
        } catch (UserException e) {
            return getMapResponseEntity(e.getMessage(), e);
        }
    }


    @ExceptionHandler(UserException.class)
    public ResponseEntity<Map<String, Object>> handleUserException(UserException ex) {
        String methodName = "handleUserException";
        LOGGER.errorLog(CLASSNAME, methodName, "UserException occurred: {}" + ex.getMessage());
        return getMapResponseEntity(ex.getMessage(), ex);
    }
}
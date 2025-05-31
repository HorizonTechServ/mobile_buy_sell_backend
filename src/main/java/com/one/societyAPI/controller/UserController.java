package com.one.societyAPI.controller;

import com.one.societyAPI.dto.AdminRegisterRequest;
import com.one.societyAPI.dto.UserDTO;
import com.one.societyAPI.dto.UserRegisterRequest;
import com.one.societyAPI.entity.User;
import com.one.societyAPI.exception.UserException;
import com.one.societyAPI.logger.DefaultLogger;
import com.one.societyAPI.repository.UserRepository;
import com.one.societyAPI.response.StandardResponse;
import com.one.societyAPI.service.OtpService;
import com.one.societyAPI.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "APIs for managing super admin, admin and users")
public class UserController {

    private static final String CLASSNAME = "UserController";
    private static final DefaultLogger LOGGER = new DefaultLogger(UserController.class);

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private OtpService otpService;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register/superAdmin")
    @Operation(summary = "Register a Super Admin", description = "Creates a new super admin with email and password")
    public ResponseEntity<StandardResponse<UserDTO>> registerSuperAdmin(@Valid @RequestBody User user) {
        String method = "registerSuperAdmin";
        LOGGER.infoLog(CLASSNAME, method, "Request to register Super Admin: {}" + user);

        try {
            UserDTO savedUser = userService.registerSuperAdmin(user);
            LOGGER.infoLog(CLASSNAME, method, "Super Admin registered: {}" + savedUser);
            return ResponseEntity.ok(StandardResponse.success("Super Admin registered", savedUser));
        } catch (UserException e) {
            LOGGER.errorLog(CLASSNAME, method, "Failed to register Super Admin: {}" + e.getMessage());
            return ResponseEntity.badRequest().body(StandardResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/register-admin")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Register an Admin", description = "Creates a new admin")
    public ResponseEntity<StandardResponse<UserDTO>> registerAdmin(@Valid @RequestBody AdminRegisterRequest request) {
        String method = "registerAdmin";
        LOGGER.infoLog(CLASSNAME, method, "Request to register Admin: {}" + request);

        try {
            User user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setMobileNumber(request.getMobileNumber());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(request.getRole());
            user.setGender(request.getGender());
            user.setLastLogin(LocalDateTime.now());

            UserDTO savedAdmin = userService.registerAdmin(user, request.getSocietyId());
            LOGGER.infoLog(CLASSNAME, method, "Admin registered: {}" + savedAdmin);
            return ResponseEntity.ok(StandardResponse.success("Admin registered", savedAdmin));
        } catch (UserException e) {
            LOGGER.errorLog(CLASSNAME, method, "Failed to register Admin: {}" + e.getMessage());
            return ResponseEntity.badRequest().body(StandardResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/register/user")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Register a User", description = "Creates a new user")
    public ResponseEntity<StandardResponse<UserDTO>> registerUser(@Valid @RequestBody UserRegisterRequest request) {
        String method = "registerUser";
        LOGGER.infoLog(CLASSNAME, method, "Request to register User: {}" + request);

        try {
            User user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setMobileNumber(request.getMobileNumber());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(request.getRole());
            user.setGender(request.getGender());
            user.setLastLogin(LocalDateTime.now());

            //UserDTO savedUser = userService.registerUser(user, request.getFlatId());
            UserDTO savedUser = userService.registerUser(user, request.getFlatId(), request.getSocietyId());

            LOGGER.infoLog(CLASSNAME, method, "User registered: {}" + savedUser);
            return ResponseEntity.ok(StandardResponse.success("User registered", savedUser));
        } catch (UserException e) {
            LOGGER.errorLog(CLASSNAME, method, "Failed to register User: {}" + e.getMessage());
            return ResponseEntity.badRequest().body(StandardResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/by-society/{societyId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Get Users by Society ID", description = "Fetch all users in a specific society")
    public ResponseEntity<StandardResponse<?>> getUsersBySociety(@PathVariable Long societyId) {
        String method = "getUsersBySociety";
        LOGGER.infoLog(CLASSNAME, method, "Fetching users for societyId: {}", societyId);
        try {
            return ResponseEntity.ok(StandardResponse.success("Users fetched", userService.getUsersBySocietyId(societyId)));
        } catch (Exception e) {
            LOGGER.errorLog(CLASSNAME, method, "Failed to fetch users: {}" + e.getMessage());
            return ResponseEntity.badRequest().body(StandardResponse.error("Error fetching users"));
        }
    }

    @GetMapping("/admin/by-society/{societyId}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Get Admin by Society ID", description = "Fetch admin assigned to a specific society")
    public ResponseEntity<StandardResponse<?>> getAdminBySociety(@PathVariable Long societyId) {
        String method = "getAdminBySociety";
        LOGGER.infoLog(CLASSNAME, method, "Fetching admin for societyId: {}", societyId);
        try {
            return ResponseEntity.ok(StandardResponse.success("Admin fetched", userService.getAdminBySocietyId(societyId)));
        } catch (UserException e) {
            LOGGER.errorLog(CLASSNAME, method, "Failed to fetch admin: {}" + e.getMessage());
            return ResponseEntity.badRequest().body(StandardResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/edit/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Edit User Details", description = "Update user name, mobile number, or flat number")
    public ResponseEntity<StandardResponse<UserDTO>> editUserDetails(
            @PathVariable Long userId,
            @RequestBody Map<String, Object> updates) {
        String method = "editUserDetails";
        LOGGER.infoLog(CLASSNAME, method, "Updating userId {} with: {}", userId);
        try {
            UserDTO updatedUser = userService.editUser(userId, updates);
            return ResponseEntity.ok(StandardResponse.success("User updated", updatedUser));
        } catch (UserException e) {
            LOGGER.errorLog(CLASSNAME, method, "Failed to update user: {}" + e.getMessage());
            return ResponseEntity.badRequest().body(StandardResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/superAdmin/edit/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Edit Super Admin Details", description = "Update Super Admin name and mobile number")
    public ResponseEntity<StandardResponse<UserDTO>> editSuperAdminDetails(
            @PathVariable Long userId,
            @RequestBody Map<String, Object> updates) {
        String method = "editSuperAdminDetails";
        LOGGER.infoLog(CLASSNAME, method, "Updating Super Admin userId {} with: {}", userId);
        try {
            UserDTO updatedUser = userService.editUser(userId, updates);
            return ResponseEntity.ok(StandardResponse.success("Super Admin updated", updatedUser));
        } catch (UserException e) {
            LOGGER.errorLog(CLASSNAME, method, "Failed to update Super Admin: {}" + e.getMessage());
            return ResponseEntity.badRequest().body(StandardResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/admin/edit/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Edit Admin Details", description = "Update Admin name and mobile number")
    public ResponseEntity<StandardResponse<UserDTO>> editAdminDetails(
            @PathVariable Long userId,
            @RequestBody Map<String, Object> updates) {
        String method = "editAdminDetails";
        LOGGER.infoLog(CLASSNAME, method, "Updating Admin userId {} with: {}" ,  userId);
        try {
            UserDTO updatedUser = userService.editUser(userId, updates);
            return ResponseEntity.ok(StandardResponse.success("Admin updated", updatedUser));
        } catch (UserException e) {
            LOGGER.errorLog(CLASSNAME, method, "Failed to update Admin: {}" + e.getMessage());
            return ResponseEntity.badRequest().body(StandardResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/change-password/send-otp")
    public ResponseEntity<?> sendOtp(@RequestParam String email) {

        if (!userRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email not registered."));
        }

        otpService.sendOtp(email);

        return ResponseEntity.ok(Map.of("message", "OTP sent to registered email"));
    }

    @PostMapping("/change-password/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestParam String email, @RequestParam String otp) {

        boolean isVerified = otpService.verifyOtp(email, otp);

        if (isVerified) {
            return ResponseEntity.ok(Map.of("message", "OTP verified successfully."));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    Map.of("error", "Invalid or expired OTP.")
            );
        }
    }


    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestParam String email,
                                            @RequestParam String newPassword) {
        if (!otpService.isOtpVerified(email)) {
            return ResponseEntity.badRequest().body(Map.of("error","OTP verification required."));
        }

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return ResponseEntity.notFound().build();

        User user = userOpt.get();

        String mobileNumber = user.getMobileNumber();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return ResponseEntity.ok(Map.of("mobileNumber", mobileNumber,
                "message", "Password changed successfully."));
    }
}
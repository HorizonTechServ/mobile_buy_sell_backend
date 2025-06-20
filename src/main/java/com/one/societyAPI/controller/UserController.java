package com.one.societyAPI.controller;

import com.one.societyAPI.dto.AdminRegisterRequest;
import com.one.societyAPI.dto.ChangePasswordRequest;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.List;
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
        LOGGER.infoLog(CLASSNAME, method, "Request to register Super Admin: " + user);

        try {
            UserDTO savedUser = userService.registerSuperAdmin(user);
            LOGGER.infoLog(CLASSNAME, method, "Super Admin registered: " + savedUser);
            return ResponseEntity.ok(StandardResponse.success("Super Admin registered", savedUser));
        } catch (UserException e) {
            LOGGER.errorLog(CLASSNAME, method, "Failed to register Super Admin: " + e.getMessage());
            return ResponseEntity.badRequest().body(StandardResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/register-admin")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Register an Admin", description = "Creates a new admin")
    public ResponseEntity<StandardResponse<UserDTO>> registerAdmin(@Valid @RequestBody AdminRegisterRequest request) {
        String method = "registerAdmin";
        LOGGER.infoLog(CLASSNAME, method, "Request to register Admin: " + request);

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
            LOGGER.infoLog(CLASSNAME, method, "Admin registered: " + savedAdmin);
            return ResponseEntity.ok(StandardResponse.success("Admin registered", savedAdmin));
        } catch (UserException e) {
            LOGGER.errorLog(CLASSNAME, method, "Failed to register Admin: " + e.getMessage());
            return ResponseEntity.badRequest().body(StandardResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/register/user")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Register a User", description = "Creates a new user")
    public ResponseEntity<StandardResponse<UserDTO>> registerUser(@Valid @RequestBody UserRegisterRequest request) {
        String method = "registerUser";
        LOGGER.infoLog(CLASSNAME, method, "Request to register User: " + request);

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

            LOGGER.infoLog(CLASSNAME, method, "User registered: " + savedUser);
            return ResponseEntity.ok(StandardResponse.success("User registered", savedUser));
        } catch (UserException e) {
            LOGGER.errorLog(CLASSNAME, method, "Failed to register User: " + e.getMessage());
            return ResponseEntity.badRequest().body(StandardResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/by-society/{societyId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Get Users by Society ID", description = "Fetch all users in a specific society")
    public ResponseEntity<StandardResponse<?>> getUsersBySociety(@PathVariable Long societyId) {
        String method = "getUsersBySociety";
        LOGGER.infoLog(CLASSNAME, method, "Fetching users for societyId: ", societyId);
        try {
            return ResponseEntity.ok(StandardResponse.success("Users fetched", userService.getUsersBySocietyId(societyId)));
        } catch (Exception e) {
            LOGGER.errorLog(CLASSNAME, method, "Failed to fetch users: " + e.getMessage());
            return ResponseEntity.badRequest().body(StandardResponse.error("Error fetching users"));
        }
    }


    @GetMapping("/admin/by-society/{societyId}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Get Admins by Society ID", description = "Fetch all admins assigned to a specific society")
    public ResponseEntity<StandardResponse<?>> getAdminsBySociety(@PathVariable Long societyId) {
        String method = "getAdminsBySociety";
        LOGGER.infoLog(CLASSNAME, method, "Fetching admins for societyId: ", societyId);
        try {
            List<UserDTO> admins = userService.getAdminsBySocietyId(societyId);
            return ResponseEntity.ok(StandardResponse.success("Admins fetched", admins));
        } catch (UserException e) {
            LOGGER.errorLog(CLASSNAME, method, "Failed to fetch admins: " + e.getMessage());
            return ResponseEntity.badRequest().body(StandardResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Delete a User", description = "Allows an Admin or Super Admin to delete a user by ID using soft delete")
    public ResponseEntity<StandardResponse<String>> deleteUser(@PathVariable Long userId) {
        String method = "deleteUser";
        LOGGER.infoLog(CLASSNAME, method, "Request to soft delete user with ID: ", userId);
        try {
            userService.softDeleteUserById(userId);
            return ResponseEntity.ok(StandardResponse.success("User deleted successfully", "Deleted userId: " + userId));
        } catch (UserException e) {
            LOGGER.errorLog(CLASSNAME, method, "Failed to delete user: " + e.getMessage());
            return ResponseEntity.badRequest().body(StandardResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/getAllUser/by-society/{societyId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Get All Users by Society ID (Role = USER)", description = "Fetch users with role USER for a specific society")
    public ResponseEntity<StandardResponse<?>> getUsersBySocietyAndRoleUser(@PathVariable Long societyId) {
        String method = "getUsersBySocietyAndRoleUser";
        LOGGER.infoLog(CLASSNAME, method, "Fetching users with role USER for societyId: ", societyId);
        try {
            return ResponseEntity.ok(StandardResponse.success("Users fetched", userService.getUsersBySocietyIdAndRoleUser(societyId)));
        } catch (Exception e) {
            LOGGER.errorLog(CLASSNAME, method, "Failed to fetch users: " + e.getMessage());
            return ResponseEntity.badRequest().body(StandardResponse.error("Error fetching users with role USER"));
        }
    }


    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'USER')")
    @Operation(summary = "Get user by userId", description = "Fetch a single user by ID without exposing password")
    public ResponseEntity<StandardResponse<UserDTO>> getUserById(@PathVariable Long userId) {
        String method = "getUserById";
        LOGGER.infoLog(CLASSNAME, method, "Fetching user by ID: " + userId);

        try {
            UserDTO userDTO = userService.getUserDetailsById(userId);
            return ResponseEntity.ok(StandardResponse.success("User fetched successfully", userDTO));
        } catch (UserException e) {
            LOGGER.errorLog(CLASSNAME, method, "Failed to fetch user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(StandardResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/edit/{userId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Edit User Details", description = "Update user name, mobile number, or flat number")
    public ResponseEntity<StandardResponse<UserDTO>> editUserDetails(
            @PathVariable Long userId,
            @RequestBody Map<String, Object> updates) {
        String method = "editUserDetails";
        LOGGER.infoLog(CLASSNAME, method, "Updating userId  with: ", userId);
        try {
            UserDTO updatedUser = userService.editUser(userId, updates);
            return ResponseEntity.ok(StandardResponse.success("User updated", updatedUser));
        } catch (UserException e) {
            LOGGER.errorLog(CLASSNAME, method, "Failed to update user: " + e.getMessage());
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
        LOGGER.infoLog(CLASSNAME, method, "Updating Super Admin userId  with: ", userId);
        try {
            UserDTO updatedUser = userService.editUser(userId, updates);
            return ResponseEntity.ok(StandardResponse.success("Super Admin updated", updatedUser));
        } catch (UserException e) {
            LOGGER.errorLog(CLASSNAME, method, "Failed to update Super Admin: " + e.getMessage());
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
        LOGGER.infoLog(CLASSNAME, method, "Updating Admin userId  with: " ,  userId);
        try {
            UserDTO updatedUser = userService.editUser(userId, updates);
            return ResponseEntity.ok(StandardResponse.success("Admin updated", updatedUser));
        } catch (UserException e) {
            LOGGER.errorLog(CLASSNAME, method, "Failed to update Admin: " + e.getMessage());
            return ResponseEntity.badRequest().body(StandardResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/reset-password/send-otp")
    public ResponseEntity<?> sendOtp(@RequestParam String email) {

        if (!userRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email not registered."));
        }

        otpService.sendOtp(email);

        return ResponseEntity.ok(Map.of("message", "OTP sent to registered email"));
    }

    @PostMapping("/reset-password/verify-otp")
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


    @PutMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email,
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


    @PostMapping("/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<StandardResponse<?>> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        String method = "changePassword";
        LOGGER.infoLog(CLASSNAME, method, "Password change request");

        try {
            // Get mobile number of currently logged-in user
            String mobileNumber = SecurityContextHolder.getContext().getAuthentication().getName();

            // Fetch user from database
            Optional<User> optionalUser = userRepository.findByMobileNumber(mobileNumber);
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(StandardResponse.error("User not found"));
            }

            User currentUser = optionalUser.get();

            // Check if old password matches
            if (!passwordEncoder.matches(request.getOldPassword(), currentUser.getPassword())) {
                return ResponseEntity.badRequest()
                        .body(StandardResponse.error("Old password is incorrect"));
            }

            // Check if new and confirm password match
            if (!request.getNewPassword().equals(request.getConfirmPassword())) {
                return ResponseEntity.badRequest()
                        .body(StandardResponse.error("New password and confirm password do not match"));
            }

            // Check if new password is same as old password
            if (passwordEncoder.matches(request.getNewPassword(), currentUser.getPassword())) {
                return ResponseEntity.badRequest()
                        .body(StandardResponse.error("New password must be different from the old password"));
            }

            // Set and encode new password
            currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(currentUser);

            return ResponseEntity.ok(StandardResponse.success("Password changed successfully", null));

        } catch (Exception e) {
            LOGGER.errorLog(CLASSNAME, method, "Password change failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(StandardResponse.error("Failed to change password"));
        }
    }


    @PostMapping("/reminder/send-maintenance-reminder/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Send maintenance reminder email to a user")
    public ResponseEntity<StandardResponse<?>> sendMaintenanceReminder(@PathVariable Long userId) {
        String method = "sendMaintenanceReminder";
        LOGGER.infoLog(CLASSNAME, method, "Sending reminder to userId: ", userId);
        try {
            userService.sendMaintenanceReminderIfPending(userId);
            return ResponseEntity.ok(StandardResponse.success("Reminder sent successfully", null));
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(StandardResponse.error(e.getMessage()));
        } catch (Exception e) {
            LOGGER.errorLog(CLASSNAME, method, "Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(StandardResponse.error("Failed to send reminder"));
        }
    }


    @PutMapping(value = "/upload-profile-picture/{userId}", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
    @Operation(summary = "Upload/Update Profile Picture", description = "Upload or update user's profile picture after registration")
    public ResponseEntity<StandardResponse<String>> uploadProfilePicture(
            @PathVariable Long userId,
            @RequestParam("image") MultipartFile imageFile) {

        String method = "uploadProfilePicture";
        LOGGER.infoLog(CLASSNAME, method, "Uploading profile picture for userId: ", userId);

        try {
            userService.updateUserProfilePicture(userId, imageFile);
            return ResponseEntity.ok(StandardResponse.success("Profile picture uploaded successfully", null));
        } catch (Exception e) {
            LOGGER.errorLog(CLASSNAME, method, "Upload failed: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(StandardResponse.error("Image upload failed"));
        }
    }

    @GetMapping("/profile-picture/{userId}")
    @PreAuthorize("hasAnyRole('USER','ADMIN','SUPER_ADMIN')")
    @Operation(summary = "Get User Profile Picture", description = "Fetch user's profile picture or error")
    public ResponseEntity<?> getUserProfilePicture(@PathVariable Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(StandardResponse.error("User not found with ID: " + userId));
        }

        byte[] imageBytes = userOpt.get().getProfilePicture();

        if (imageBytes == null || imageBytes.length == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(StandardResponse.error("No profile picture found for user ID: " + userId));
        }

        // Auto-detect content type
        String contentType = "application/octet-stream";
        try (InputStream is = new ByteArrayInputStream(imageBytes)) {
            String guessed = URLConnection.guessContentTypeFromStream(is);
            if (guessed != null) contentType = guessed;
        } catch (IOException ignored) {}

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }
}
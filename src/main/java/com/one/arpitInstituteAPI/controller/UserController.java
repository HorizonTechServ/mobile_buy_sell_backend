package com.one.arpitInstituteAPI.controller;

import com.one.arpitInstituteAPI.dto.*;
import com.one.arpitInstituteAPI.entity.User;
import com.one.arpitInstituteAPI.exception.UserException;
import com.one.arpitInstituteAPI.logger.DefaultLogger;
import com.one.arpitInstituteAPI.repository.UserRepository;
import com.one.arpitInstituteAPI.response.StandardResponse;
import com.one.arpitInstituteAPI.service.OtpService;
import com.one.arpitInstituteAPI.service.UserService;
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


    @PostMapping("/register-admin")
    @Operation(summary = "Register an Admin", description = "Creates a new admin")
    public ResponseEntity<StandardResponse<UserDTO>> registerAdmin(@Valid @RequestBody RegisterRequest request) {
        String method = "registerAdmin";
        LOGGER.infoLog(CLASSNAME, method, "Request to register Admin: " + request);

        try {
            User userAdmin = registerUsers(request);
            UserDTO savedAdmin = userService.registerAdmin(userAdmin);
            LOGGER.infoLog(CLASSNAME, method, "Admin registered: " + savedAdmin);
            return ResponseEntity.ok(StandardResponse.success("Admin registered", savedAdmin));
        } catch (UserException e) {
            LOGGER.errorLog(CLASSNAME, method, "Failed to register Admin: " + e.getMessage());
            return ResponseEntity.badRequest().body(StandardResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/register-student")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    @Operation(summary = "Register an Student", description = "Creates a new student")
    public ResponseEntity<StandardResponse<UserDTO>> registerStudent(@Valid @RequestBody RegisterRequest request) {
        String method = "registerStudent";

        LOGGER.infoLog(CLASSNAME, method, "Request to register Student: " + request);

        try {
            User user = registerUsers(request);
            UserDTO savedUser = userService.registerStudent(user);
            LOGGER.infoLog(CLASSNAME, method, "Student registered: " + savedUser);
            return ResponseEntity.ok(StandardResponse.success("Student registered", savedUser));
        } catch (UserException e) {
            LOGGER.errorLog(CLASSNAME, method, "Failed to register Student: " + e.getMessage());
            return ResponseEntity.badRequest().body(StandardResponse.error(e.getMessage()));
        }
    }


    @DeleteMapping("/delete/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
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


    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
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

    @PutMapping("/edit-student/{userId}")
    @PreAuthorize("hasAnyRole('STUDENT','ADMIN')")
    @Operation(summary = "Edit User Details", description = "Update user name, mobile number")
    public ResponseEntity<StandardResponse<UserDTO>> editStudentDetails(
            @PathVariable Long userId,
            @RequestBody Map<String, Object> updates) {
        String method = "editStudentDetails";
        LOGGER.infoLog(CLASSNAME, method, "Updating userId  with: ", userId);
        try {
            UserDTO updatedStudent = userService.editStudent(userId, updates);
            return ResponseEntity.ok(StandardResponse.success("User updated", updatedStudent));
        } catch (UserException e) {
            LOGGER.errorLog(CLASSNAME, method, "Failed to update Student: " + e.getMessage());
            return ResponseEntity.badRequest().body(StandardResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/edit-admin/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Edit Admin Details", description = "Update Admin name, mobile number")
    public ResponseEntity<StandardResponse<UserDTO>> editAdminDetails(
            @PathVariable Long userId,
            @RequestBody Map<String, Object> updates) {
        String method = "editAdminDetails";
        LOGGER.infoLog(CLASSNAME, method, "Updating userId  with: ", userId);
        try {
            UserDTO updatedAdmin = userService.editAdmin(userId, updates);
            return ResponseEntity.ok(StandardResponse.success("Admin updated", updatedAdmin));
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

        String mobileNumber = user.getUsername();
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
            String username = SecurityContextHolder.getContext().getAuthentication().getName();

            // Fetch user from database
            Optional<User> optionalUser = userRepository.findUserByUsername(username);
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


    @PutMapping(value = "/upload-profile-picture/{userId}", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyRole('STUDENT','ADMIN')")
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
    @PreAuthorize("hasAnyRole('STUDENT','ADMIN')")
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


    private User registerUsers(@RequestBody @Valid RegisterRequest request) {
        User userAdmin = new User();
        userAdmin.setEmail(request.getEmail());
        userAdmin.setUsername(request.getUsername());
        userAdmin.setMobileNumber(request.getMobileNumber());
        userAdmin.setPassword(passwordEncoder.encode(request.getPassword()));
        userAdmin.setRole(request.getRole());
        userAdmin.setGender(request.getGender());
        userAdmin.setLastLogin(LocalDateTime.now());

        return userAdmin;
    }
}
package com.one.societyAPI.controller;

import com.one.societyAPI.dto.LoginRequest;
import com.one.societyAPI.dto.RefreshTokenRequest;
import com.one.societyAPI.entity.FcmToken;
import com.one.societyAPI.entity.User;
import com.one.societyAPI.logger.DefaultLogger;
import com.one.societyAPI.repository.FcmTokenRepository;
import com.one.societyAPI.repository.UserRepository;
import com.one.societyAPI.response.StandardResponse;
import com.one.societyAPI.service.UserService;
import com.one.societyAPI.utils.JwtUtil;
import com.one.societyAPI.utils.UserRole;
import com.one.societyAPI.utils.UserStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication Management (Generate Token)", description = "APIs for generating a token using user ID and password, and verifying if the user exists")
public class AuthController {

    private static final String CLASSNAME = "AuthController";
    private static final DefaultLogger LOGGER = new DefaultLogger(AuthController.class);

    private final UserService userService;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final FcmTokenRepository fcmTokenRepository;

    public AuthController(UserService userService, UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, FcmTokenRepository fcmTokenRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.fcmTokenRepository = fcmTokenRepository;
    }

    @GetMapping("/check-user/{mobileNumber}")
    public ResponseEntity<StandardResponse<Void>> checkUserUnique(@PathVariable String mobileNumber) {
        String strMethodName = "checkUserUnique";
        LOGGER.infoLog(CLASSNAME, strMethodName, "Received request to check user existence: " + mobileNumber);

        if (userService.isUserIdUnique(mobileNumber)) {
            LOGGER.debugLog(CLASSNAME, strMethodName, "User ID is available" + mobileNumber);
            return ResponseEntity.ok(StandardResponse.success("User ID is available", null));
        } else {
            LOGGER.warnLog(CLASSNAME, strMethodName, "User ID already exists" + mobileNumber);
            return ResponseEntity.badRequest().body(StandardResponse.error("User ID already exists"));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<StandardResponse<Map<String, Object>>> login(@Valid @RequestBody LoginRequest request) {
        String method = "login";
        LOGGER.infoLog(CLASSNAME, method, "Received login request for user: " + request.getMobileNumber());

        Optional<User> userOptional = userRepository.findByMobileNumber(request.getMobileNumber());
        if (userOptional.isEmpty()) {
            LOGGER.warnLog(CLASSNAME, method, "Login failed - User not found: " + request.getMobileNumber());
            return ResponseEntity.status(401).body(StandardResponse.error("Invalid Credentials"));
        }

        User user = userOptional.get();
        if (user.getStatus() != UserStatus.ACTIVE) {
            LOGGER.warnLog(CLASSNAME, method, "Login denied - User status is not ACTIVE: " + user.getStatus());
            return ResponseEntity.status(403).body(StandardResponse.error("Account is " + user.getStatus() + ". Please contact support."));
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            LOGGER.warnLog(CLASSNAME, method, "Invalid password attempt for user: " + request.getMobileNumber());
            return ResponseEntity.status(401).body(StandardResponse.error("Invalid Credentials"));
        }

        String token = jwtUtil.generateToken(user.getMobileNumber());
        Date expirationDate = jwtUtil.extractExpiration(token);


        // Refresh Token Code
        String refreshToken = jwtUtil.generateRefreshToken(user.getMobileNumber());

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("token", token);
        responseData.put("expires_at", expirationDate.toString());
        responseData.put("time", expirationDate.getTime());
        responseData.put("mobileNumber", user.getMobileNumber());
        responseData.put("email", user.getEmail());
        responseData.put("name", user.getName());
        responseData.put("role", user.getRole().name());

        // Added Refresh Token Fields
        responseData.put("refreshToken", refreshToken);

        responseData.put("userId", user.getId());
        if (user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.USER) {
            responseData.put("societyId", user.getSociety().getId());
        }

        LOGGER.infoLog(CLASSNAME, method, "Login successful for user: " + request.getMobileNumber());
        return ResponseEntity.ok(StandardResponse.success("Login successfully", responseData));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout user and remove FCM token")
    public ResponseEntity<StandardResponse<String>> logout(
            @RequestParam Long userId,
            @RequestParam String fcmToken) {

        String method = "logout";
        LOGGER.infoLog(CLASSNAME, method, "Logout request from userId: " + userId);

        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            LOGGER.warnLog(CLASSNAME, method, "User not found for ID: " + userId);
            return ResponseEntity.status(404).body(StandardResponse.error("User not found"));
        }

        Optional<FcmToken> tokenOpt = fcmTokenRepository.findByToken(fcmToken);
        if (tokenOpt.isPresent()) {
            FcmToken token = tokenOpt.get();
            if (token.getUser().getId().equals(userId)) {
                fcmTokenRepository.delete(token);
                LOGGER.infoLog(CLASSNAME, method, "FCM token removed successfully for userId: " + userId);
            } else {
                LOGGER.warnLog(CLASSNAME, method, "Token-user mismatch: token not deleted.");
                return ResponseEntity.status(400).body(StandardResponse.error("Token doesn't belong to the user"));
            }
        } else {
            LOGGER.warnLog(CLASSNAME, method, "FCM token not found for removal.");
        }

        return ResponseEntity.ok(StandardResponse.success("Logout successful and token removed", userOpt.toString()));
    }



    @PostMapping("/refresh-token")
    @Operation(summary = "Generate new access token using a valid refresh token")
    public ResponseEntity<StandardResponse<Map<String, Object>>> refreshToken(@RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        try {
            // Extract username and check if refresh token is valid
            String username = jwtUtil.extractUsername(refreshToken);

            if (!jwtUtil.isTokenValid(refreshToken, username)) {
                return ResponseEntity.status(401).body(StandardResponse.error("Invalid or expired refresh token"));

            }

            // Generate new access token and new refresh token
            String newAccessToken = jwtUtil.generateToken(username);
            String newRefreshToken = jwtUtil.generateRefreshToken(username);

            Map<String, Object> response = new HashMap<>();
            response.put("access_token", newAccessToken);
            response.put("refresh_token", newRefreshToken);
            response.put("expires_at", jwtUtil.extractExpiration(newRefreshToken).getTime());

            return ResponseEntity.ok(StandardResponse.success("Refresh Token successfully", response));


        } catch (Exception e) {
            return ResponseEntity.status(401).body(StandardResponse.error("Could not refresh token"));

        }
    }

}
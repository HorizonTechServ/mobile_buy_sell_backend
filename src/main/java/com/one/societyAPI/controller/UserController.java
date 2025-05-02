package com.one.societyAPI.controller;
import com.one.societyAPI.entity.User;
import com.one.societyAPI.exception.UserException;
import com.one.societyAPI.logger.DefaultLogger;
import com.one.societyAPI.repository.UserRepository;
import com.one.societyAPI.service.UserService;
import com.one.societyAPI.utils.UserStatus;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.one.societyAPI.exception.GlobalExceptionHandler.getMapResponseEntity;


@RestController
@RequestMapping("/users")
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {

    private static final String CLASSNAME = "UserController";

    private static final DefaultLogger LOGGER = new DefaultLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;


    private final PasswordEncoder passwordEncoder;

    public UserController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user with email and password")
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {

        String methodName = "registerUser";

        LOGGER.infoLog(CLASSNAME, methodName, "Received request to register a new user: " + user);

        User savedUser = userService.registerUser(user);

        LOGGER.infoLog(CLASSNAME, methodName, "User Register Successfully : " + user);

        return ResponseEntity.ok(savedUser);
    }

    @PatchMapping("/update")
    @Operation(summary = "Update user profile", description = "Allows a user to partially update profile information")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateUserProfile(@RequestBody User updatedFields) {
        String methodName = "updateUserProfile";

        LOGGER.infoLog(CLASSNAME, methodName, "Received PATCH update for user: " + updatedFields);

        User updatedUser = userService.updateUser(updatedFields);

        LOGGER.infoLog(CLASSNAME, methodName, "User profile patched for user: " + updatedFields);

        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/delete/{mobileNumber}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteUserAccount(@PathVariable String mobileNumber) {

        Optional<User> optionalUser = userRepository.findByMobileNumber(mobileNumber);

        Map<String, String> response = new HashMap<>();

        if (optionalUser.isPresent()) {

            User user = optionalUser.get();

            if (user.getStatus() == UserStatus.ACTIVE) {
                user.setStatus(UserStatus.DELETE);
                userRepository.save(user);

                response.put("message", "User account marked as DELETED.");
                return ResponseEntity.ok(response);
            } else {
                response.put("error", "User is not ACTIVE.");
                return ResponseEntity.badRequest().body(response);
            }

        } else {
            response.put("error", "User not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/{mobileNumber}")
   // @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Get user details", description = "Returns user profile based on mobileNumber")
    public ResponseEntity<?> getUserByMobileNumber(@PathVariable String mobileNumber) {
        String methodName = "getUserById";
        LOGGER.infoLog(CLASSNAME, methodName, "Fetching user with ID: " + mobileNumber);

        Optional<User> userOpt = userRepository.findByMobileNumber(mobileNumber);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return ResponseEntity.ok(user);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("error", "User not found.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // Global Exception Handling for UserException
    @ExceptionHandler(UserException.class)
    public ResponseEntity<Map<String, Object>> handleUserException(UserException ex) {
        String methodName = "handleUserException";
        LOGGER.errorLog(CLASSNAME, methodName, "UserException occurred: " + ex.getMessage());

        return getMapResponseEntity(ex.getMessage(), ex);
    }
}
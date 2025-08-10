package com.one.mobilebuysellAPI.controller;

import com.one.mobilebuysellAPI.dto.LoginRequest;
import com.one.mobilebuysellAPI.entity.User;
import com.one.mobilebuysellAPI.repository.UserRepository;
import com.one.mobilebuysellAPI.response.StandardResponse;
import com.one.mobilebuysellAPI.service.UserService;
import com.one.mobilebuysellAPI.utils.JwtUtil;
import com.one.mobilebuysellAPI.utils.UserRole;
import com.one.mobilebuysellAPI.utils.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private User getTestUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("encodedpassword");
        user.setEmail("testuser@example.com");
        user.setRole(UserRole.ADMIN);
        user.setStatus(UserStatus.ACTIVE);
        user.setAdmin(true);
        return user;
    }


    @Test
    void login_userNotFound_returns401() {
        LoginRequest request = new LoginRequest();
        request.setUsername("nonexistent");
        request.setPassword("password");

        when(userRepository.findUserByUsername("nonexistent")).thenReturn(Optional.empty());

        ResponseEntity<StandardResponse<java.util.Map<String, Object>>> response = authController.login(request);

        assertEquals(401, response.getStatusCodeValue());
        assertTrue(response.getBody().getMessage().contains("Invalid Credentials"));
    }

    @Test
    void login_userNotActive_returns403() {
        User user = getTestUser();
        user.setStatus(UserStatus.INACTIVE);

        when(userRepository.findUserByUsername("testuser")).thenReturn(Optional.of(user));

        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        ResponseEntity<StandardResponse<java.util.Map<String, Object>>> response = authController.login(request);

        assertEquals(403, response.getStatusCodeValue());
        assertTrue(response.getBody().getMessage().contains("Account is INACTIVE"));
    }

    @Test
    void login_invalidPassword_returns401() {
        User user = getTestUser();

        when(userRepository.findUserByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", user.getPassword())).thenReturn(false);

        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("wrongpassword");

        ResponseEntity<StandardResponse<java.util.Map<String, Object>>> response = authController.login(request);

        assertEquals(401, response.getStatusCodeValue());
        assertTrue(response.getBody().getMessage().contains("Invalid Credentials"));
    }

    @Test
    void login_success_returnsTokenAndUserDetails() {
        User user = getTestUser();
        String jwtToken = "jwt-token";
        String refreshToken = "refresh-token";
        Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 60);

        when(userRepository.findUserByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", user.getPassword())).thenReturn(true);
        when(jwtUtil.generateToken("testuser")).thenReturn(jwtToken);
        when(jwtUtil.extractExpiration(jwtToken)).thenReturn(expiration);
        when(jwtUtil.generateRefreshToken("testuser")).thenReturn(refreshToken);

        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        ResponseEntity<StandardResponse<java.util.Map<String, Object>>> response = authController.login(request);

        assertEquals(200, response.getStatusCodeValue());
        StandardResponse<java.util.Map<String, Object>> body = response.getBody();
        assertEquals("Login successfully", body.getMessage());
        assertNotNull(body.getData());
        assertEquals(jwtToken, body.getData().get("token"));
        assertEquals("testuser", body.getData().get("username"));
        assertEquals("testuser@example.com", body.getData().get("email"));
        assertEquals("ADMIN", body.getData().get("role"));
        assertEquals(true, body.getData().get("admin"));
        assertEquals(refreshToken, body.getData().get("refreshToken"));
        assertEquals(1L, body.getData().get("userId"));
        assertEquals(expiration.toString(), body.getData().get("expires_at"));
        assertEquals(expiration.getTime(), body.getData().get("time"));
    }
}
package com.one.mobilebuysellAPI.service;

import com.one.mobilebuysellAPI.dto.UserDTO;
import com.one.mobilebuysellAPI.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface UserService {

    boolean isUserIdUnique(String mobileNumber);

    public UserDTO registerAdmin(User user);

    UserDTO editAdmin(Long userId, Map<String, Object> updates);

    void softDeleteUserById(Long userId);

    UserDTO getUserDetailsById(Long userId);

    public void updateUserProfilePicture(Long userId, MultipartFile file) throws IOException;

}
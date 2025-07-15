package com.one.arpitInstituteAPI.service;

import com.one.arpitInstituteAPI.dto.UserDTO;
import com.one.arpitInstituteAPI.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface UserService {

    boolean isUserIdUnique(String mobileNumber);

    public UserDTO registerAdmin(User user);

    public UserDTO registerStudent(User user);

    UserDTO editAdmin(Long userId, Map<String, Object> updates);

    UserDTO editStudent(Long userId, Map<String, Object> updates);

    void softDeleteUserById(Long userId);

    UserDTO getUserDetailsById(Long userId);

    public void updateUserProfilePicture(Long userId, MultipartFile file) throws IOException;

}
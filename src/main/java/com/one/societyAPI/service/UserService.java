package com.one.societyAPI.service;

import com.one.societyAPI.dto.UserDTO;
import com.one.societyAPI.entity.User;

import java.util.List;
import java.util.Map;

public interface UserService {

    boolean isUserIdUnique(String mobileNumber);

    UserDTO registerSuperAdmin(User user);

    public UserDTO registerAdmin(User user, Long societyId);

    public UserDTO registerUser(User user, Long flatId);

    List<UserDTO> getUsersBySocietyId(Long societyId);

    UserDTO getAdminBySocietyId(Long societyId);

    UserDTO editUser(Long userId, Map<String, Object> updates);

}
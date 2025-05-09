package com.one.societyAPI.service;

import com.one.societyAPI.dto.UserDTO;
import com.one.societyAPI.entity.User;

public interface UserService {

    boolean isUserIdUnique(String mobileNumber);

    UserDTO registerSuperAdmin(User user);

    UserDTO registerAdmin(User user);

    UserDTO registerUser(User user);

    UserDTO updateUser(User updatedUserData);
}
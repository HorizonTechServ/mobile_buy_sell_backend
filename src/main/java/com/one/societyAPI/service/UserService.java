package com.one.societyAPI.service;

import com.one.societyAPI.dto.UserDTO;
import com.one.societyAPI.entity.User;

public interface UserService {

    boolean isUserIdUnique(String mobileNumber);

    UserDTO registerSuperAdmin(User user);

    public UserDTO registerAdmin(User user, Long societyId);

    public UserDTO registerUser(User user, Long flatId);

    UserDTO updateUser(User updatedUserData);
}
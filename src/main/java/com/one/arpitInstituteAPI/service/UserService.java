package com.one.arpitInstituteAPI.service;

import com.one.arpitInstituteAPI.dto.UserDTO;
import com.one.arpitInstituteAPI.entity.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface UserService {

    boolean isUserIdUnique(String mobileNumber);

    UserDTO registerSuperAdmin(User user);

    public UserDTO registerAdmin(User user, Long societyId);

    public UserDTO registerUser(User user, Long flatId, Long societyId);

    List<UserDTO> getUsersBySocietyId(Long societyId);

    public List<UserDTO> getAdminsBySocietyId(Long societyId);

    UserDTO editUser(Long userId, Map<String, Object> updates);

    void softDeleteUserById(Long userId);

    List<UserDTO> getUsersBySocietyIdAndRoleUser(Long societyId);

    public void sendMaintenanceReminderIfPending(Long userId);

    UserDTO getUserDetailsById(Long userId);

    public void updateUserProfilePicture(Long userId, MultipartFile file) throws IOException;

}
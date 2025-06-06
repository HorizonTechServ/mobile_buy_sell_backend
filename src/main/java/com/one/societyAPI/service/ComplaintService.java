package com.one.societyAPI.service;

import com.one.societyAPI.dto.ComplaintDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ComplaintService {
    public ComplaintDTO createComplaint(Long userId, String description, MultipartFile image);
    public ComplaintDTO resolveComplaint(Long complaintId, Long resolverId, String description, MultipartFile image);
    List<ComplaintDTO> getComplaintsByUser(Long userId);
    List<ComplaintDTO> getComplaintsBySociety(Long societyId);
    List<ComplaintDTO> getOpenComplaintsBySociety(Long societyId);
    List<ComplaintDTO> getResolvedComplaintsBySociety(Long societyId);
}
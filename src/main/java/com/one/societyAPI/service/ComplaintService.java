package com.one.societyAPI.service;

import com.one.societyAPI.dto.ComplaintDTO;

import java.util.List;

public interface ComplaintService {
    ComplaintDTO createComplaint(Long userId, String description);
    ComplaintDTO resolveComplaint(Long complaintId, Long resolverId);
    List<ComplaintDTO> getComplaintsByUser(Long userId);
    List<ComplaintDTO> getComplaintsBySociety(Long societyId);
    List<ComplaintDTO> getOpenComplaintsBySociety(Long societyId);
    List<ComplaintDTO> getResolvedComplaintsBySociety(Long societyId);
}
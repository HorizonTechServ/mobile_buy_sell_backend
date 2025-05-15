package com.one.societyAPI.service.impl;

import com.one.societyAPI.dto.ComplaintDTO;
import com.one.societyAPI.entity.Complaint;
import com.one.societyAPI.entity.User;
import com.one.societyAPI.repository.ComplaintRepository;
import com.one.societyAPI.repository.UserRepository;
import com.one.societyAPI.service.ComplaintService;
import com.one.societyAPI.utils.ComplaintStatus;
import com.one.societyAPI.utils.UserRole;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;

    public ComplaintServiceImpl(ComplaintRepository complaintRepository, UserRepository userRepository) {
        this.complaintRepository = complaintRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ComplaintDTO createComplaint(Long userId, String description) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Complaint complaint = new Complaint();
        complaint.setComplaintBy(user);
        complaint.setDescription(description);
        complaint.setComplaintDate(LocalDateTime.now());

        return toDTO(complaintRepository.save(complaint));
    }

    @Override
    public ComplaintDTO resolveComplaint(Long complaintId, Long resolverId) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        User resolver = userRepository.findById(resolverId)
                .orElseThrow(() -> new RuntimeException("Resolver not found"));

        if (resolver.getRole().equals(UserRole.USER)) {
            throw new RuntimeException("Only Admin or Super Admin can resolve complaints");
        }

        complaint.setResolvedBy(resolver);
        complaint.setStatus(ComplaintStatus.RESOLVED);
        complaint.setResolvedDate(LocalDateTime.now());

        return toDTO(complaintRepository.save(complaint));
    }

    @Override
    public List<ComplaintDTO> getComplaintsByUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return complaintRepository.findByComplaintBy(user)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ComplaintDTO> getComplaintsBySociety(Long societyId) {
        return complaintRepository.findBySocietyId(societyId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }


    private ComplaintDTO toDTO(Complaint c) {
        User complaintBy = c.getComplaintBy();
        User resolvedBy = c.getResolvedBy();

        return new ComplaintDTO(
                c.getId(),
                complaintBy.getId(),
                complaintBy.getName(),
                complaintBy.getFlat() != null ? complaintBy.getFlat().getFlatNumber() : null,
                resolvedBy != null ? resolvedBy.getId() : null,
                resolvedBy != null ? resolvedBy.getName() : null,
                c.getStatus(),
                c.getDescription(),
                c.getComplaintDate(),
                c.getResolvedDate()
        );
    }
}
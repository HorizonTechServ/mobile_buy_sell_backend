package com.one.societyAPI.service.impl;

import com.one.societyAPI.dto.ComplaintDTO;
import com.one.societyAPI.entity.Complaint;
import com.one.societyAPI.entity.ComplaintImage;
import com.one.societyAPI.entity.User;
import com.one.societyAPI.exception.UserException;
import com.one.societyAPI.repository.ComplaintRepository;
import com.one.societyAPI.repository.UserRepository;
import com.one.societyAPI.service.ComplaintImageService;
import com.one.societyAPI.service.ComplaintService;
import com.one.societyAPI.utils.ComplaintStatus;
import com.one.societyAPI.utils.UserRole;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final UserRepository userRepository;
    private final ComplaintImageService complaintImageService;


    public ComplaintServiceImpl(ComplaintRepository complaintRepository, UserRepository userRepository, ComplaintImageService complaintImageService) {
        this.complaintRepository = complaintRepository;
        this.userRepository = userRepository;
        this.complaintImageService = complaintImageService;
    }

    @Override
    public ComplaintDTO createComplaint(Long userId, String description, MultipartFile image) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));


        Complaint complaint = new Complaint();
        complaint.setComplaintBy(user); // <-- Fix: set the user object
        complaint.setDescription(description);
        complaint.setStatus(ComplaintStatus.OPEN);
        complaint.setComplaintDate(LocalDateTime.now()); // if you track timestamps

        if (image != null && !image.isEmpty()) {
            try {
                ComplaintImage savedImage = complaintImageService.saveImage(image);
                complaint.setImage(savedImage);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save complaint image", e);
            }
        }

        Complaint savedComplaint = complaintRepository.save(complaint);

        return toDTO(savedComplaint);
    }

    @Override
    public ComplaintDTO resolveComplaint(Long complaintId, Long resolverId, String description, MultipartFile image) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found with complaintId : " + complaintId));

        User resolver = userRepository.findById(resolverId)
                .orElseThrow(() -> new UserException("Resolver not found with resolverId : " + resolverId));

        if (resolver.getRole().equals(UserRole.USER)) {
            throw new UserException("Only Admin or Super Admin can resolve complaints");
        }

        complaint.setResolvedBy(resolver);
        complaint.setStatus(ComplaintStatus.RESOLVED);
        complaint.setResolvedDate(LocalDateTime.now());

        if (description != null && !description.isBlank()) {
            complaint.setDescription(description); // override complaint with final resolution description
        }

        if (image != null && !image.isEmpty()) {
            try {
                ComplaintImage savedImage = complaintImageService.saveImage(image);
                complaint.setImage(savedImage);
            } catch (IOException e) {
                throw new RuntimeException("Failed to save complaint image during resolution", e);
            }
        }

        return toDTO(complaintRepository.save(complaint));
    }


    @Override
    public List<ComplaintDTO> getComplaintsByUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User not found with userId : " + userId));

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

    @Override
    public List<ComplaintDTO> getOpenComplaintsBySociety(Long societyId) {
        return complaintRepository.findOpenComplaintsBySocietyId(societyId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ComplaintDTO> getResolvedComplaintsBySociety(Long societyId) {
        return complaintRepository.findResolvedComplaintsBySocietyId(societyId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private ComplaintDTO toDTO(Complaint c) {
        String imageBase64 = null;
        Long imageId = null;

        if (c.getImage() != null && c.getImage().getId() != null) {
            try {
                byte[] imageBytes = complaintImageService.getImage(c.getImage().getId());
                String base64 = Base64.getEncoder().encodeToString(imageBytes);
                String mimeType = "image/png"; // Or detect dynamically from contentType
                imageBase64 = "data:" + mimeType + ";base64," + base64;
                imageId = c.getImage().getId();
            } catch (Exception ex) {
                imageBase64 = null;
            }
        }

        return new ComplaintDTO(
                c.getId(),
                c.getComplaintBy() != null ? c.getComplaintBy().getId() : null,
                c.getComplaintBy() != null ? c.getComplaintBy().getName() : null,
                c.getComplaintBy().getFlat() != null ? c.getComplaintBy().getFlat().getFlatNumber() : null,
                c.getResolvedBy() != null ? c.getResolvedBy().getId() : null,
                c.getResolvedBy() != null ? c.getResolvedBy().getName() : null,
                c.getStatus(),
                c.getDescription(),
                c.getComplaintDate(),
                c.getResolvedDate(),
                imageBase64,
                imageId
        );
    }

}
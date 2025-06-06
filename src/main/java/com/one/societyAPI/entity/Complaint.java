package com.one.societyAPI.entity;

import com.one.societyAPI.utils.ComplaintStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User complaintBy;

    @ManyToOne
    private User resolvedBy;

    @Enumerated(EnumType.STRING)
    private ComplaintStatus status = ComplaintStatus.OPEN;

    @Column(nullable = false)
    private String description;

    private LocalDateTime complaintDate;

    private LocalDateTime resolvedDate;

    private String flatNumber; // Optional for faster reference

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", referencedColumnName = "id")
    private ComplaintImage image;

    @Transient
    private Long complaintImageId;

    public Long getComplaintImageId() {
        return image != null ? image.getId() : null;
    }

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getComplaintBy() {
        return complaintBy;
    }

    public void setComplaintBy(User complaintBy) {
        this.complaintBy = complaintBy;
    }

    public User getResolvedBy() {
        return resolvedBy;
    }

    public void setResolvedBy(User resolvedBy) {
        this.resolvedBy = resolvedBy;
    }

    public ComplaintStatus getStatus() {
        return status;
    }

    public void setStatus(ComplaintStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getComplaintDate() {
        return complaintDate;
    }

    public void setComplaintDate(LocalDateTime complaintDate) {
        this.complaintDate = complaintDate;
    }

    public LocalDateTime getResolvedDate() {
        return resolvedDate;
    }

    public void setResolvedDate(LocalDateTime resolvedDate) {
        this.resolvedDate = resolvedDate;
    }

    public String getFlatNumber() {
        return flatNumber;
    }

    public void setFlatNumber(String flatNumber) {
        this.flatNumber = flatNumber;
    }

    public ComplaintImage getImage() {
        return image;
    }

    public void setImage(ComplaintImage image) {
        this.image = image;
    }

    public void setComplaintImageId(Long complaintImageId) {
        this.complaintImageId = complaintImageId;
    }
}
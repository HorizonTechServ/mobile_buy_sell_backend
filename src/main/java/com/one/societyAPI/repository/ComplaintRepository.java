package com.one.societyAPI.repository;

import com.one.societyAPI.entity.Complaint;
import com.one.societyAPI.entity.User;
import com.one.societyAPI.utils.ComplaintStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    List<Complaint> findByComplaintBy(User user);
    List<Complaint> findByComplaintBy_Society_Id(Long societyId);
    List<Complaint> findByStatus(ComplaintStatus status);

    @Query("SELECT c FROM Complaint c " +
            "JOIN c.complaintBy u " +
            "JOIN u.flat f " +
            "JOIN f.society s " +
            "WHERE s.id = :societyId")
    List<Complaint> findBySocietyId(@Param("societyId") Long societyId);

}
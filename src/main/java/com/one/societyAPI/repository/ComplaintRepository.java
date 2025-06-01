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


    @Query("SELECT c FROM Complaint c " +
            "LEFT JOIN c.complaintBy.flat f " +
            "WHERE c.status = 'OPEN' AND " +
            "(f.society.id = :societyId OR c.complaintBy.society.id = :societyId)")
    List<Complaint> findOpenComplaintsBySocietyId(@Param("societyId") Long societyId);


    @Query("SELECT c FROM Complaint c WHERE c.status = 'RESOLVED' AND c.complaintBy.flat.society.id = :societyId")
    List<Complaint> findResolvedComplaintsBySocietyId(@Param("societyId") Long societyId);

}
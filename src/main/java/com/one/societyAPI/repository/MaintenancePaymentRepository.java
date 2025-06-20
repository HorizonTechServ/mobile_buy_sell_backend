package com.one.societyAPI.repository;

import com.one.societyAPI.entity.Maintenance;
import com.one.societyAPI.entity.MaintenancePayment;
import com.one.societyAPI.entity.User;
import com.one.societyAPI.utils.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MaintenancePaymentRepository extends JpaRepository<MaintenancePayment, Long> {
    List<MaintenancePayment> findByMaintenance(Maintenance maintenance);
    Optional<MaintenancePayment> findByMaintenanceAndUser(Maintenance maintenance, User user);
    List<MaintenancePayment> findByMaintenanceAndStatus(Maintenance maintenance, PaymentStatus status);

    List<MaintenancePayment> findByUser_Flat_Society_IdAndStatus(Long societyId, PaymentStatus status);

    List<MaintenancePayment> findByUser_Flat_Society_IdAndMaintenance_Id(Long societyId, Long maintenanceId);

    @Query("SELECT p FROM MaintenancePayment p " +
            "WHERE p.maintenance = :maintenance " +
            "AND p.status = :status " +
            "AND (:month IS NULL OR FUNCTION('MONTH', p.maintenance.dueDate) = :month) " +
            "AND (:year IS NULL OR FUNCTION('YEAR', p.maintenance.dueDate) = :year)")
    List<MaintenancePayment> findByMaintenanceStatusAndMonthYear(
            @Param("maintenance") Maintenance maintenance,
            @Param("status") PaymentStatus status,
            @Param("month") Integer month,
            @Param("year") Integer year
    );

    List<MaintenancePayment> findByUser(User user);

    List<MaintenancePayment> findByUserIdAndStatus(Long userId, PaymentStatus status);

    @Query("SELECT m FROM MaintenancePayment m WHERE m.user.id = :userId AND m.status = :status AND MONTH(m.maintenance.dueDate) = :month AND YEAR(m.maintenance.dueDate) = :year")
    List<MaintenancePayment> findByUserIdAndStatusAndMonthAndYear(@Param("userId") Long userId,
                                                                  @Param("status") PaymentStatus status,
                                                                  @Param("month") int month,
                                                                  @Param("year") int year);


}
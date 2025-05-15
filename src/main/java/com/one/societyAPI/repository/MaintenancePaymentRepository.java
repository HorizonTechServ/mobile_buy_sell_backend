package com.one.societyAPI.repository;

import com.one.societyAPI.entity.Maintenance;
import com.one.societyAPI.entity.MaintenancePayment;
import com.one.societyAPI.entity.User;
import com.one.societyAPI.utils.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MaintenancePaymentRepository extends JpaRepository<MaintenancePayment, Long> {
    List<MaintenancePayment> findByMaintenance(Maintenance maintenance);
    Optional<MaintenancePayment> findByMaintenanceAndUser(Maintenance maintenance, User user);
    List<MaintenancePayment> findByMaintenanceAndStatus(Maintenance maintenance, PaymentStatus status);
}
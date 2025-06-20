package com.one.societyAPI.service.impl;

import com.one.societyAPI.dto.MaintenancePaymentDTO;
import com.one.societyAPI.entity.Maintenance;
import com.one.societyAPI.entity.MaintenancePayment;
import com.one.societyAPI.entity.User;
import com.one.societyAPI.exception.MaintenancePaymentException;
import com.one.societyAPI.exception.UserException;
import com.one.societyAPI.repository.MaintenancePaymentRepository;
import com.one.societyAPI.repository.MaintenanceRepository;
import com.one.societyAPI.repository.UserRepository;
import com.one.societyAPI.service.MaintenancePaymentService;
import com.one.societyAPI.utils.PaymentStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaintenancePaymentServiceImpl implements MaintenancePaymentService {

    private final MaintenanceRepository maintenanceRepository;
    private final UserRepository userRepository;
    private final MaintenancePaymentRepository paymentRepository;

    public MaintenancePaymentServiceImpl(MaintenanceRepository maintenanceRepository, UserRepository userRepository, MaintenancePaymentRepository paymentRepository) {
        this.maintenanceRepository = maintenanceRepository;
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public List<MaintenancePaymentDTO> getPaymentsByMaintenance(Long maintenanceId) {
        Maintenance maintenance = maintenanceRepository.findById(maintenanceId)
                .orElseThrow(() -> new MaintenancePaymentException("Maintenance not found with maintenanceId: " + maintenanceId));

        return paymentRepository.findByMaintenance(maintenance)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MaintenancePaymentDTO updatePaymentStatus(Long maintenanceId, Long userId, String statusStr) {
        Maintenance maintenance = maintenanceRepository.findById(maintenanceId)
                .orElseThrow(() -> new MaintenancePaymentException("Maintenance not found with maintenanceId: " + maintenanceId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException("User not found with UserId: " + userId));

        PaymentStatus status = PaymentStatus.valueOf(statusStr.toUpperCase());

        MaintenancePayment payment = paymentRepository.findByMaintenanceAndUser(maintenance, user)
                .orElseThrow(() -> new MaintenancePaymentException("Payment record not found"));

        payment.setStatus(status);

        payment.setPaymentDate(status == PaymentStatus.PAID ? LocalDate.now() : null);

        return toDTO(paymentRepository.save(payment));
    }

    @Override
    public List<MaintenancePaymentDTO> getPaymentsByStatus(Long maintenanceId, String status, Integer month, Integer year) {
        Maintenance maintenance = maintenanceRepository.findById(maintenanceId)
                .orElseThrow(() -> new MaintenancePaymentException("Maintenance not found with maintenanceId: " + maintenanceId));

        PaymentStatus paymentStatus = PaymentStatus.valueOf(status.toUpperCase());

        List<MaintenancePayment> payments = paymentRepository
                .findByMaintenanceStatusAndMonthYear(maintenance, paymentStatus, month, year);

        return payments.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaintenancePaymentDTO> getPendingPaymentsByUserId(Long userId, Integer month, Integer year) {
        List<MaintenancePayment> payments;

        if (month != null && year != null) {
            payments = paymentRepository.findByUserIdAndStatusAndMonthAndYear(userId, PaymentStatus.PENDING, month, year);
        } else {
            payments = paymentRepository.findByUserIdAndStatus(userId, PaymentStatus.PENDING);
        }

        return payments.stream()
                .map(this::toDTO)
                .toList();
    }



    private MaintenancePaymentDTO toDTO(MaintenancePayment p) {
        return new MaintenancePaymentDTO(
                p.getId(),
                p.getMaintenance().getId(),
                p.getUser().getId(),
                p.getUser().getName(),
                p.getStatus(),
                p.getPaymentDate(),
                p.getMaintenance().getDueDate(),
                p.getUser().getFlat().getFlatNumber(),
                p.getMaintenance().getAmount()
                );
    }
}
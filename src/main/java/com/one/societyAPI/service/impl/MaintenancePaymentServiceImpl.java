package com.one.societyAPI.service.impl;

import com.one.societyAPI.dto.MaintenancePaymentDTO;
import com.one.societyAPI.entity.Maintenance;
import com.one.societyAPI.entity.MaintenancePayment;
import com.one.societyAPI.entity.User;
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
                .orElseThrow(() -> new RuntimeException("Maintenance not found"));

        return paymentRepository.findByMaintenance(maintenance)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public MaintenancePaymentDTO updatePaymentStatus(Long maintenanceId, Long userId, String statusStr) {
        Maintenance maintenance = maintenanceRepository.findById(maintenanceId)
                .orElseThrow(() -> new RuntimeException("Maintenance not found"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        PaymentStatus status = PaymentStatus.valueOf(statusStr.toUpperCase());

        MaintenancePayment payment = paymentRepository.findByMaintenanceAndUser(maintenance, user)
                .orElseThrow(() -> new RuntimeException("Payment record not found"));

        payment.setStatus(status);
        payment.setPaymentDate(status == PaymentStatus.PAID ? LocalDate.now() : null);

        return toDTO(paymentRepository.save(payment));
    }

    @Override
    public List<MaintenancePaymentDTO> getPaymentsByStatus(Long maintenanceId, String status, Integer month, Integer year) {
        Maintenance maintenance = maintenanceRepository.findById(maintenanceId)
                .orElseThrow(() -> new RuntimeException("Maintenance not found"));

        PaymentStatus paymentStatus = PaymentStatus.valueOf(status.toUpperCase());

        List<MaintenancePayment> payments = paymentRepository
                .findByMaintenanceStatusAndMonthYear(maintenance, paymentStatus, month, year);

        return payments.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
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
                p.getUser().getFlat().getFlatNumber()
        );
    }
}
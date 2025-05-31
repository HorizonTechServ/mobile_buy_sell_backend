package com.one.societyAPI.service.impl;

import com.one.societyAPI.dto.MaintenanceDTO;
import com.one.societyAPI.entity.Maintenance;
import com.one.societyAPI.entity.MaintenancePayment;
import com.one.societyAPI.entity.Society;
import com.one.societyAPI.entity.User;
import com.one.societyAPI.exception.MaintenanceException;
import com.one.societyAPI.repository.MaintenancePaymentRepository;
import com.one.societyAPI.repository.MaintenanceRepository;
import com.one.societyAPI.repository.SocietyRepository;
import com.one.societyAPI.repository.UserRepository;
import com.one.societyAPI.service.MaintenanceService;
import com.one.societyAPI.utils.PaymentStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaintenanceServiceImpl implements MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final MaintenancePaymentRepository maintenancePaymentRepository;
    private final UserRepository userRepository;


    private final SocietyRepository societyRepository;

    public MaintenanceServiceImpl(MaintenanceRepository maintenanceRepository, MaintenancePaymentRepository maintenancePaymentRepository, UserRepository userRepository, SocietyRepository societyRepository) {
        this.maintenanceRepository = maintenanceRepository;
        this.maintenancePaymentRepository = maintenancePaymentRepository;
        this.userRepository = userRepository;
        this.societyRepository = societyRepository;
    }


    @Override
    public MaintenanceDTO createMaintenance(MaintenanceDTO maintenanceDTO) {
        Society society = societyRepository.findById(maintenanceDTO.societyId())
                .orElseThrow(() -> new MaintenanceException("Society not found with societyId: " + maintenanceDTO.societyId()));

        Maintenance maintenance = new Maintenance();
        maintenance.setDescription(maintenanceDTO.description());
        maintenance.setAmount(maintenanceDTO.amount());
        maintenance.setDueDate(maintenanceDTO.dueDate());
        maintenance.setSociety(society);

        Maintenance savedMaintenance = maintenanceRepository.save(maintenance);

        // Get all users from the society & create payment records
        List<User> users = userRepository.findByFlat_Society_Id(society.getId());

        for (User user : users) {
            MaintenancePayment payment = new MaintenancePayment();
            payment.setMaintenance(savedMaintenance);
            payment.setUser(user);
            payment.setStatus(PaymentStatus.PENDING); // default
            maintenancePaymentRepository.save(payment);
        }

        return mapToDTO(savedMaintenance);
    }


    @Override
    public List<MaintenanceDTO> getMaintenanceBySociety(Long societyId) {
        return maintenanceRepository.findBySociety_Id(societyId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MaintenanceDTO> getMaintenanceByStatus(Long societyId, PaymentStatus status) {
        List<MaintenancePayment> payments = maintenancePaymentRepository.findByUser_Flat_Society_IdAndStatus(societyId, status);

        return payments.stream()
                .map(MaintenancePayment::getMaintenance)
                .distinct()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void updateStatusForMaintenance(Long societyId, Long maintenanceId, PaymentStatus status) {
        List<MaintenancePayment> payments = maintenancePaymentRepository
                .findByUser_Flat_Society_IdAndMaintenance_Id(societyId, maintenanceId);

        if (payments.isEmpty()) {
            throw new MaintenanceException("No payments found for given society and maintenance");
        }

        for (MaintenancePayment payment : payments) {
            payment.setStatus(status);
        }
        maintenancePaymentRepository.saveAll(payments);
    }



    private MaintenanceDTO toDTO(Maintenance maintenance) {
        return new MaintenanceDTO(
                maintenance.getId(),
                maintenance.getDescription(),
                maintenance.getAmount(),
                maintenance.getDueDate(),
                maintenance.getSociety().getId(),
                maintenance.getStatus()
        );
    }

    private MaintenanceDTO mapToDTO(Maintenance maintenance) {
        return new MaintenanceDTO(
                maintenance.getId(),
                maintenance.getDescription(),
                maintenance.getAmount(),
                maintenance.getDueDate(),
                maintenance.getSociety().getId(),
                maintenance.getStatus()
        );
    }
}
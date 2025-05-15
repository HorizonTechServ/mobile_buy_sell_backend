package com.one.societyAPI.service.impl;

import com.one.societyAPI.dto.MaintenanceDTO;
import com.one.societyAPI.entity.Maintenance;
import com.one.societyAPI.entity.MaintenancePayment;
import com.one.societyAPI.entity.Society;
import com.one.societyAPI.entity.User;
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
                .orElseThrow(() -> new RuntimeException("Society not found"));

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

    private MaintenanceDTO toDTO(Maintenance maintenance) {
        return new MaintenanceDTO(
                maintenance.getId(),
                maintenance.getDescription(),
                maintenance.getAmount(),
                maintenance.getDueDate(),
                maintenance.getSociety().getId()
        );
    }

    private MaintenanceDTO mapToDTO(Maintenance maintenance) {
        return new MaintenanceDTO(
                maintenance.getId(),
                maintenance.getDescription(),
                maintenance.getAmount(),
                maintenance.getDueDate(),
                maintenance.getSociety().getId()
        );
    }
}
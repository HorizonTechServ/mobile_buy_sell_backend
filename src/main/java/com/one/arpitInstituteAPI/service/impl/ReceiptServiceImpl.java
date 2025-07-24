package com.one.arpitInstituteAPI.service.impl;

import com.one.arpitInstituteAPI.dto.ReceiptRequest;
import com.one.arpitInstituteAPI.entity.Receipt;
import com.one.arpitInstituteAPI.entity.ReceiptConfig;
import com.one.arpitInstituteAPI.entity.Student;
import com.one.arpitInstituteAPI.exception.DuplicateFeePaymentException;
import com.one.arpitInstituteAPI.repository.ReceiptConfigRepository;
import com.one.arpitInstituteAPI.repository.ReceiptRepository;
import com.one.arpitInstituteAPI.repository.StudentRepository;
import com.one.arpitInstituteAPI.service.ReceiptService;
import com.one.arpitInstituteAPI.utils.MoneyToWordsConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ReceiptServiceImpl implements ReceiptService {

    private final ReceiptRepository receiptRepository;

    private final ReceiptConfigRepository receiptConfigRepository;

    private final StudentRepository studentRepository;

    @Autowired
    public ReceiptServiceImpl(ReceiptRepository receiptRepository, ReceiptConfigRepository receiptConfigRepository, StudentRepository studentRepository) {
        this.receiptRepository = receiptRepository;
        this.receiptConfigRepository = receiptConfigRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public Receipt createReceipt(ReceiptRequest request, String createdBy) {

        // Check for existing payment
        Optional<Receipt> existingReceipt = receiptRepository.findByStudentNameAndSemesterAndDepartment(
                request.getStudentName(),
                request.getSemester(),
                request.getDepartment()
        );

        if (existingReceipt.isPresent()) {
            throw new DuplicateFeePaymentException(
                    request.getStudentName() + " has already paid the fees for Semester " + request.getSemester()
            );
        }

        // Prepare receipt entity
        Receipt receipt = new Receipt();
        receipt.setReceiptNo(generateNextReceiptNo());
        receipt.setYear(request.getYear());
        receipt.setDate(request.getDate());
        receipt.setStudentName(request.getStudentName());
        receipt.setStudentMobileNumber(request.getStudentMobileNumber());
        receipt.setStudentEmail(request.getStudentEmail());

        receipt.setDepartment(request.getDepartment());
        receipt.setBranch(request.getBranch());
        receipt.setSemester(request.getSemester());

        receipt.setTuitionFees(request.getTuitionFees());
        receipt.setStudentDevFees(request.getStudentDevFees());
        receipt.setLabLibSportsFees(request.getLabLibSportsFees());
        receipt.setOtherFees(request.getOtherFees());

        double total = request.getTuitionFees() + request.getStudentDevFees()
                + request.getLabLibSportsFees() + request.getOtherFees();

        receipt.setTotalAmount(total);
        receipt.setAmountInWords(MoneyToWordsConverter.convert(total));

        receipt.setPaymentMode(request.getPaymentMode());
        receipt.setChequeNo(request.getChequeNo());
        receipt.setChequeDate(request.getChequeDate());
        receipt.setStatus("PAID");
        receipt.setCreatedBy(createdBy);
        receipt.setCreatedAt(LocalDateTime.now());

        // ✅ Save receipt first
        Receipt savedReceipt = receiptRepository.save(receipt);

        // ✅ Save student only if receipt saved successfully
        studentRepository.findByStudentName(request.getStudentName())
                .orElseGet(() -> {
                    Student student = new Student();
                    student.setStudentName(request.getStudentName());
                    student.setStudentMobileNumber(request.getStudentMobileNumber());
                    student.setStudentEmail(request.getStudentEmail());
                    return studentRepository.save(student);
                });

        return savedReceipt;
    }

    private String generateNextReceiptNo() {
        String defaultPrefix = "RECX";
        String prefix = defaultPrefix;

        // 1. Use latest ReceiptConfig if available
        Optional<ReceiptConfig> optionalConfig = receiptConfigRepository.findTopByOrderByIdDesc();
        if (optionalConfig.isPresent() && optionalConfig.get().getReceiptPrefix() != null) {
            prefix = optionalConfig.get().getReceiptPrefix().trim();
        }

        // 2. Get the last receipt with the same prefix
        String lastReceiptNo = receiptRepository.findLastReceiptNoByPrefix(prefix + "-"); // e.g., "RECX-000123"

        long nextNumber = 1;
        if (lastReceiptNo != null && lastReceiptNo.startsWith(prefix + "-")) {
            try {
                String numberPart = lastReceiptNo.substring((prefix + "-").length());
                nextNumber = Long.parseLong(numberPart) + 1;
            } catch (NumberFormatException e) {
                // fallback to count + 1 (for same prefix)
                nextNumber = receiptRepository.countByPrefix(prefix + "-") + 1;
            }
        }

        return String.format("%s-%06d", prefix, nextNumber);
    }

}
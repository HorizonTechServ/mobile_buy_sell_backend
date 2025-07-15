package com.one.arpitInstituteAPI.service.impl;

import com.one.arpitInstituteAPI.dto.ReceiptRequest;
import com.one.arpitInstituteAPI.entity.Receipt;
import com.one.arpitInstituteAPI.repository.ReceiptRepository;
import com.one.arpitInstituteAPI.service.ReceiptService;
import com.one.arpitInstituteAPI.utils.MoneyToWordsConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReceiptServiceImpl implements ReceiptService {

    private final ReceiptRepository receiptRepository;

    @Autowired
    public ReceiptServiceImpl(ReceiptRepository receiptRepository) {
        this.receiptRepository = receiptRepository;
    }

    @Override
    public Receipt createReceipt(ReceiptRequest request, String createdBy) {
        Receipt receipt = new Receipt();
        receipt.setReceiptNo(request.getReceiptNo());
        receipt.setYear(request.getYear());
        receipt.setDate(request.getDate());
        receipt.setStudentName(request.getStudentName());
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

        return receiptRepository.save(receipt);
    }
}
package com.one.arpitInstituteAPI.service;

import com.one.arpitInstituteAPI.dto.ReceiptRequest;
import com.one.arpitInstituteAPI.entity.Receipt;

public interface ReceiptService {
    Receipt createReceipt(ReceiptRequest request, String createdBy);
}
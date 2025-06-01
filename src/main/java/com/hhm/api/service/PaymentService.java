package com.hhm.api.service;

import com.hhm.api.model.dto.request.CreateVNPayPaymentURLRequest;

public interface PaymentService {
    String createVNPayPaymentURL(CreateVNPayPaymentURLRequest request);
}

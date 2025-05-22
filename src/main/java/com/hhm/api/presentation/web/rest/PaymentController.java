package com.hhm.api.presentation.web.rest;

import com.hhm.api.model.dto.request.CreateVNPayPaymentURLRequest;
import com.hhm.api.model.dto.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Payment Resources")
@RequestMapping("/api/payment")
@Validated
public interface PaymentController {
    @Operation(summary = "Create VNPay payment URL")
    @GetMapping("/vnpay")
    Response<String> createVNPayPaymentURL(CreateVNPayPaymentURLRequest request);
}

package com.hhm.api.presentation.web.rest.impl;

import com.hhm.api.model.dto.request.CreateVNPayPaymentURLRequest;
import com.hhm.api.model.dto.response.Response;
import com.hhm.api.presentation.web.rest.PaymentController;
import com.hhm.api.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentControllerImpl implements PaymentController {
    private final PaymentService paymentService;

    @Override
    public Response<String> createVNPayPaymentURL(CreateVNPayPaymentURLRequest request) {
        return Response.of(paymentService.createVNPayPaymentURL(request));
    }
}

package com.hhm.api.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateVNPayPaymentURLRequest {
    @NotBlank(message = "ORDER_INFO_REQUIRED")
    private String orderInfo;

    @NotNull(message = "PAYMENT_AMOUNT_REQUIRED")
    private Long amount;

    @NotBlank(message = "LANGUAGE_REQUIRED")
    private String language;
}

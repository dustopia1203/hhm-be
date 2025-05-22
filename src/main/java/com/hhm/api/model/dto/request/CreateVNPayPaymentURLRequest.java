package com.hhm.api.model.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateVNPayPaymentURLRequest {
    private String orderInfo;
    private Long amount;
    private String language;
}

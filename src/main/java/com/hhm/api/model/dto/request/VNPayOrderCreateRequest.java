package com.hhm.api.model.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class VNPayOrderCreateRequest extends OrderCreateRequest {
    private String transactionNumber;
}

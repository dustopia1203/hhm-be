package com.hhm.api.model.dto.response;

import com.hhm.api.support.enums.PaymentMethod;
import com.hhm.api.support.enums.TransactionStatus;
import com.hhm.api.support.enums.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class TransactionResponse {
    private UUID id;
    private UUID userId;
    private String username;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private TransactionStatus transactionStatus;
    private TransactionType transactionType;
    private String referenceContext;
}

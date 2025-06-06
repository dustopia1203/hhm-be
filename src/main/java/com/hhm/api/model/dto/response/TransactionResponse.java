package com.hhm.api.model.dto.response;

import com.hhm.api.support.enums.PaymentMethod;
import com.hhm.api.support.enums.TransactionStatus;
import com.hhm.api.support.enums.TransactionType;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class TransactionResponse {
    private UUID id;
    private UUID userId;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private TransactionStatus transactionStatus;
    private TransactionType transactionType;
    private Instant createdAt;
}

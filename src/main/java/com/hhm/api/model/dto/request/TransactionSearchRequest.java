package com.hhm.api.model.dto.request;

import com.hhm.api.support.enums.PaymentMethod;
import com.hhm.api.support.enums.TransactionStatus;
import com.hhm.api.support.enums.TransactionType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class TransactionSearchRequest extends PagingRequest {
    private List<UUID> ids;
    private List<UUID> userIds;
    private List<PaymentMethod> paymentMethods;
    private List<TransactionStatus> transactionStatuses;
    private List<TransactionType> transactionTypes;
}

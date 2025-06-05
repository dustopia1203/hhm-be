package com.hhm.api.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class AccountBalanceResponse {
    private BigDecimal balance;
}

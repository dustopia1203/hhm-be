package com.hhm.api.model.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderItemCreateRequest {
    private UUID productId;
    private BigDecimal price;
    private Integer amount;
}

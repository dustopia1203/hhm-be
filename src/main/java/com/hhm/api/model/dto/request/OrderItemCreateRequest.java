package com.hhm.api.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderItemCreateRequest {
    @NotNull(message = "PRODUCT_REQUIRED")
    private UUID productId;

    @NotNull(message = "ORDER_ITEM_PRICE_REQUIRED")
    private BigDecimal price;

    @NotNull(message = "ORDER_ITEM_AMOUNT_REQUIRED")
    private Integer amount;
}

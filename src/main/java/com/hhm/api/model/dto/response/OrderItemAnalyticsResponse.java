package com.hhm.api.model.dto.response;

import com.hhm.api.support.enums.OrderItemStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
@Data
@Builder
public class OrderItemAnalyticsResponse {
    private UUID id;
    private UUID userId;
    private UUID productId;
    private UUID shippingId;
    private String username;
    private BigDecimal price;
    private BigDecimal shippingPrice;
    private Integer amount;
    private String address;
    private OrderItemStatus orderItemStatus;
    private String productName;
    private Instant createdAt;
}

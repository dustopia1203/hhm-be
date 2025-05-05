package com.hhm.api.model.dto.response;

import com.hhm.api.support.enums.OrderItemStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class OrderItemResponse {
    private UUID id;
    private UUID shopId;
    private UUID productId;
    private UUID shippingId;
    private String shopName;
    private BigDecimal price;
    private Integer amount;
    private String address;
    private OrderItemStatus orderItemStatus;
    private String productName;
    private String productImage;
}

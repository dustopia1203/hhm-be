package com.hhm.api.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class CartItemResponse {
    private UUID id;
    private UUID productId;
    private String shopName;
    private String name;
    private List<String> images;
    private BigDecimal price;
    private BigDecimal salePrice;
    private Float salePercent;
    private Integer amount;
    private Integer leftCount;
}

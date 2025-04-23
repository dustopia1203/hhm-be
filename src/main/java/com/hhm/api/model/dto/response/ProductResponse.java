package com.hhm.api.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ProductResponse {
    private UUID id;
    private UUID shopId;
    private UUID categoryId;
    private String name;
    private String description;
    private List<String> images;
    private BigDecimal price;
    private BigDecimal salePrice;
    private Float salePercent;
    private Integer amount;
    private Integer soldCount;
    private Long reviewCount;
    private Float rating;
    private CategoryResponse category;
    private Instant createdAt;
}

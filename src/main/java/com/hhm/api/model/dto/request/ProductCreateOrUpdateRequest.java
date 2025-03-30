package com.hhm.api.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProductCreateOrUpdateRequest extends Request {
    @NotNull(message = "SHOP_ID_REQUIRED")
    private UUID shopId;

    @NotNull(message = "CATEGORY_ID_REQUIRED")
    private UUID categoryId;

    @NotNull(message = "PRODUCT_NAME_REQUIRED")
    private String name;

    private String description;
    private String contentUrls;

    @NotNull(message = "PRODUCT_PRICE_REQUIRED")
    private BigDecimal price;

    @NotNull(message = "PRODUCT_AMOUNT_REQUIRED")
    private Integer amount;
}

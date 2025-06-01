package com.hhm.api.model.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
public class ShippingCreateOrUpdateRequest extends Request {
    @NotBlank(message = "SHIPPING_NAME_REQUIRED")
    private String name;

    private String avatarUrl;

    private String duration;

    @NotNull(message = "SHIPPING_PRICE_REQUIRED")
    private BigDecimal price;
}

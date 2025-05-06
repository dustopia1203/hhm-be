package com.hhm.api.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class AddCartRequest extends Request {
    @NotNull(message = "PRODUCT_REQUIRED")
    private UUID productId;

    @Min(
            value = 1,
            message = "PRODUCT_AMOUNT_MIN"
    )
    private Integer amount;
}

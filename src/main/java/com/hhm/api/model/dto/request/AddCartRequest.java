package com.hhm.api.model.dto.request;

import com.hhm.api.support.constants.ValidateConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class AddCartRequest extends Request  {
    @NotNull(message = "PRODUCT_ID_REQUIRED")
    private UUID productId;

    @Size(
            min = ValidateConstraint.Length.PRODUCT_AMOUNT_MIN_LENGTH,
            message = "PRODUCT_AMOUNT_REQUIRED"
    )
    private Integer amount;
}

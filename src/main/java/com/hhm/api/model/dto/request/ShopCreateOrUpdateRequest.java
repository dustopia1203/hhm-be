package com.hhm.api.model.dto.request;

import com.hhm.api.support.constants.ValidateConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ShopCreateOrUpdateRequest extends Request {
    @NotBlank(message = "SHOP_NAME_REQUIRED")
    @Size(
            max = ValidateConstraint.Length.NAME_MAX_LENGTH,
            message = "NAME_LENGTH"
    )
    private String name;

    @NotBlank(message = "SHOP_ADDRESS_REQUIRED")
    @Size(
            max = ValidateConstraint.Length.ADDRESS_MAX_LENGTH,
            message = "ADDRESS_LENGTH"
    )
    private String address;

    private String avatarUrl;
}

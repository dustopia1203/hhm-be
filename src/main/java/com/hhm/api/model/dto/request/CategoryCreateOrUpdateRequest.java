package com.hhm.api.model.dto.request;

import com.hhm.api.support.constants.ValidateConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class CategoryCreateOrUpdateRequest extends Request {
    private UUID parentId;

    @NotBlank(message = "CATEGORY_NAME_REQUIRED")
    @Size(
            max = ValidateConstraint.Length.NAME_MAX_LENGTH,
            message = "CATEGORY_NAME_LENGTH"
    )
    private String name;
}

package com.hhm.api.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RefundRequest extends Request {
    @NotNull(message = "REASON_REQUIRED")
    private String reason;

    @NotNull(message = "REFUND_IMAGES_REQUIRED")
    private String images;

    private String note;
}

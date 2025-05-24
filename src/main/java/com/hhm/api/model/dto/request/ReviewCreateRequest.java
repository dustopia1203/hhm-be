package com.hhm.api.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReviewCreateRequest extends Request {
    @NotNull(message = "ORDER_ITEM_REQUIRED")
    private UUID orderItemId;

    @NotNull(message = "RATING_REQUIRED")
    private Float rating;

    private String description;

    private String contentUrls;
}

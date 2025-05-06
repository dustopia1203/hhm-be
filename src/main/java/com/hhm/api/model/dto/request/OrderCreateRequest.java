package com.hhm.api.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderCreateRequest extends Request {
    @NotNull(message = "SHIPPING_REQUIRED")
    private UUID shippingId;

    @NotBlank(message = "ADDRESS_REQUIRED")
    private String address;

    @NotEmpty(message = "ORDER_ITEMS_REQUIRED")
    private List<OrderItemCreateRequest> orderItemCreateRequests;
}

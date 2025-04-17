package com.hhm.api.model.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class CartItemRequest extends Request  {
    private UUID productId;
    private Integer amount;
}

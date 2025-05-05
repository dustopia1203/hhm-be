package com.hhm.api.model.dto.request;

import com.hhm.api.support.enums.OrderItemStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class OrderItemSearchRequest extends PagingRequest {
    private List<UUID> ids;
    private List<UUID> userIds;
    private List<UUID> shopIds;
    private List<UUID> shippingIds;
    private OrderItemStatus orderItemStatus;
}

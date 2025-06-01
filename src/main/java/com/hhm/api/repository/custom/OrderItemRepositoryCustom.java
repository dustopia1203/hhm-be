package com.hhm.api.repository.custom;

import com.hhm.api.model.dto.request.OrderItemSearchRequest;
import com.hhm.api.model.entity.OrderItem;

import java.util.List;

public interface OrderItemRepositoryCustom {
    Long count(OrderItemSearchRequest request);

    List<OrderItem> search(OrderItemSearchRequest request);
}

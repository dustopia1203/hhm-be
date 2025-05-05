package com.hhm.api.service;

import com.hhm.api.model.dto.PageDTO;
import com.hhm.api.model.dto.request.OrderCreateRequest;
import com.hhm.api.model.dto.request.OrderItemSearchRequest;
import com.hhm.api.model.dto.response.OrderItemResponse;
import com.hhm.api.model.entity.OrderItem;

import java.util.List;

public interface OrderService {
    PageDTO<OrderItemResponse> searchOrderItem(OrderItemSearchRequest request);

    PageDTO<OrderItemResponse> searchMyOrderItem(OrderItemSearchRequest request);

    PageDTO<OrderItemResponse> searchMyShopOrderItem(OrderItemSearchRequest request);

    List<OrderItem> createMy(OrderCreateRequest request);
}

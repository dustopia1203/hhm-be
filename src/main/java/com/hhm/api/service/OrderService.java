package com.hhm.api.service;

import com.hhm.api.model.dto.PageDTO;
import com.hhm.api.model.dto.request.OrderCreateRequest;
import com.hhm.api.model.dto.request.OrderItemSearchRequest;
import com.hhm.api.model.dto.request.RefundRequest;
import com.hhm.api.model.dto.request.VNPayOrderCreateRequest;
import com.hhm.api.model.dto.response.OrderItemResponse;
import com.hhm.api.model.entity.OrderItem;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    PageDTO<OrderItemResponse> searchOrderItem(OrderItemSearchRequest request);

    PageDTO<OrderItemResponse> searchMyOrderItem(OrderItemSearchRequest request);

    PageDTO<OrderItemResponse> searchMyShopOrderItem(OrderItemSearchRequest request);

    List<OrderItem> codPaymentMyOrder(OrderCreateRequest request);

    List<OrderItem> vnPayPaymentMyOrder(VNPayOrderCreateRequest request);

    void refundMy(UUID id, RefundRequest request);

    void completedMy(UUID id);
}

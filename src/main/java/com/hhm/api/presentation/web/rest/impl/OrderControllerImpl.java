package com.hhm.api.presentation.web.rest.impl;

import com.hhm.api.model.dto.request.OrderCreateRequest;
import com.hhm.api.model.dto.request.OrderItemSearchRequest;
import com.hhm.api.model.dto.request.RefundRequest;
import com.hhm.api.model.dto.request.VNPayOrderCreateRequest;
import com.hhm.api.model.dto.response.OrderItemResponse;
import com.hhm.api.model.dto.response.PagingResponse;
import com.hhm.api.model.dto.response.Response;
import com.hhm.api.model.entity.OrderItem;
import com.hhm.api.presentation.web.rest.OrderController;
import com.hhm.api.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class OrderControllerImpl implements OrderController {
    private final OrderService orderService;

    @Override
    public PagingResponse<OrderItemResponse> searchOrderItem(OrderItemSearchRequest request) {
        return PagingResponse.of(orderService.searchOrderItem(request));
    }

    @Override
    public PagingResponse<OrderItemResponse> searchMyOrderItem(OrderItemSearchRequest request) {
        return PagingResponse.of(orderService.searchMyOrderItem(request));
    }

    @Override
    public PagingResponse<OrderItemResponse> searchMyShopOrderItem(OrderItemSearchRequest request) {
        return PagingResponse.of(orderService.searchMyShopOrderItem(request));
    }

    @Override
    public Response<List<OrderItem>> codPaymentMyOrder(OrderCreateRequest request) {
        return Response.of(orderService.codPaymentMyOrder(request));
    }

    @Override
    public Response<List<OrderItem>> vnPayPaymentMyOrder(VNPayOrderCreateRequest request) {
        return Response.of(orderService.vnPayPaymentMyOrder(request));
    }

    @Override
    public Response<Boolean> refundMy(UUID id, RefundRequest request) {
        orderService.refundMy(id, request);
        return Response.ok();
    }

    @Override
    public Response<Boolean> completedMy(UUID id) {
        orderService.completedMy(id);
        return Response.ok();
    }
}

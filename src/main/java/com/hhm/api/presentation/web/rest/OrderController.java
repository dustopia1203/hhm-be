package com.hhm.api.presentation.web.rest;

import com.hhm.api.config.application.validator.ValidatePaging;
import com.hhm.api.model.dto.request.OrderCreateRequest;
import com.hhm.api.model.dto.request.OrderItemSearchRequest;
import com.hhm.api.model.dto.request.RefundRequest;
import com.hhm.api.model.dto.request.SolanaOrderCreateRequest;
import com.hhm.api.model.dto.request.VNPayOrderCreateRequest;
import com.hhm.api.model.dto.response.OrderItemResponse;
import com.hhm.api.model.dto.response.PagingResponse;
import com.hhm.api.model.dto.response.Response;
import com.hhm.api.model.entity.OrderItem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Tag(name = "Order Resources")
@RequestMapping("/api/orders")
@Validated
public interface OrderController {
    @Operation(summary = "Search order items")
    @GetMapping("/q")
    @PreAuthorize("hasPermission(null, 'ORDER:READ')")
    PagingResponse<OrderItemResponse> searchOrderItem(@ValidatePaging(sortModel = OrderItem.class) OrderItemSearchRequest request);

    @Operation(summary = "Search my order items")
    @GetMapping("/my/q")
    PagingResponse<OrderItemResponse> searchMyOrderItem(@ValidatePaging(sortModel = OrderItem.class) OrderItemSearchRequest request);

    @Operation(summary = "Search my shop order items")
    @GetMapping("/my/shop/q")
    PagingResponse<OrderItemResponse> searchMyShopOrderItem(@ValidatePaging(sortModel = OrderItem.class) OrderItemSearchRequest request);

    @Operation(summary = "COD payment for my order")
    @PostMapping("/my/payment/cod")
    Response<List<OrderItem>> codPaymentMyOrder(@Valid @RequestBody OrderCreateRequest request);

    @Operation(summary = "VNPay payment for my order")
    @PostMapping("/my/payment/vnpay")
    Response<List<OrderItem>> vnPayPaymentMyOrder(@Valid @RequestBody VNPayOrderCreateRequest request);

    @Operation(summary = "Solana payment for my order")
    @PostMapping("/my/payment/solana")
    Response<List<OrderItem>> solanaPaymentMyOrder(@Valid @RequestBody SolanaOrderCreateRequest request);

    @Operation(summary = "Refund my order")
    @PostMapping("/my/{id}/refund")
    Response<Boolean> refundMy(@PathVariable UUID id,
                               @RequestBody RefundRequest request);

    @Operation(summary = "Completed my order")
    @PostMapping("/my/{id}/completed")
    Response<Boolean> completedMy(@PathVariable UUID id);
}

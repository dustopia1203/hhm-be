package com.hhm.api.presentation.web.rest;

import com.hhm.api.config.application.validator.ValidatePaging;
import com.hhm.api.model.dto.request.OrderCreateRequest;
import com.hhm.api.model.dto.request.OrderItemSearchRequest;
import com.hhm.api.model.dto.response.OrderItemResponse;
import com.hhm.api.model.dto.response.PagingResponse;
import com.hhm.api.model.dto.response.Response;
import com.hhm.api.model.entity.OrderItem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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

    @Operation(summary = "Create my order")
    @PostMapping("/my")
    Response<List<OrderItem>> createMy(@RequestBody OrderCreateRequest request);
}

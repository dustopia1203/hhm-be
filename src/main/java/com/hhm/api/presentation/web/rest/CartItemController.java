package com.hhm.api.presentation.web.rest;

import com.hhm.api.model.dto.request.CartItemRequest;
import com.hhm.api.model.dto.request.IdsRequest;
import com.hhm.api.model.dto.response.CartItemResponse;
import com.hhm.api.model.dto.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Tag(name = "CartItem Resources")
@RequestMapping("/api/cart_items")
@Validated
public interface CartItemController {
    @Operation(summary = "Add Item to My Cart")
    @PostMapping("/add")
    Response<Boolean> addItemToMyCart(@Valid @RequestBody CartItemRequest request);

    @Operation(summary = "Get Item To Cart")
    @GetMapping("/{cartId}")
    Response<List<CartItemResponse>> getMyCart(@PathVariable UUID cartId);

    @Operation(summary = "Delete Item From Cart")
    @DeleteMapping("/d")
    Response<Boolean> deleteItemsFromMyCart(@RequestBody IdsRequest request);
}

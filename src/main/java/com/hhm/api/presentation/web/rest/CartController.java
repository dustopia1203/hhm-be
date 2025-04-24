package com.hhm.api.presentation.web.rest;

import com.hhm.api.model.dto.request.AddCartRequest;
import com.hhm.api.model.dto.request.IdsRequest;
import com.hhm.api.model.dto.response.CartItemResponse;
import com.hhm.api.model.dto.response.Response;
import com.hhm.api.model.entity.CartItem;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "Cart Resources")
@RequestMapping("/api/carts")
@Validated
public interface CartController {
    @Operation(summary = "Add to my cart")
    @PostMapping("/my")
    Response<Boolean> addToMyCart(@Valid @RequestBody AddCartRequest request);

    @Operation(summary = "Get Item To Cart")
    @GetMapping("/my")
    Response<List<CartItemResponse>> getMyCart();

    @Operation(summary = "Delete from my cart")
    @DeleteMapping("/my")
    Response<Boolean> deleteFromMyCart(@RequestBody IdsRequest request);
}

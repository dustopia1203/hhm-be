package com.hhm.api.presentation.web.rest.impl;

import com.hhm.api.model.dto.request.CartItemRequest;
import com.hhm.api.model.dto.request.IdsRequest;
import com.hhm.api.model.dto.response.CartItemResponse;
import com.hhm.api.model.dto.response.Response;
import com.hhm.api.presentation.web.rest.CartItemController;
import com.hhm.api.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CartItemControllerImpl implements CartItemController {
    private final CartItemService cartItemService;

    @Override
    public Response<Boolean> addItemToMyCart(CartItemRequest request) {
        cartItemService.addItemsToMyCart(request);
        return Response.ok();
    }

    @Override
    public Response<List<CartItemResponse>> getMyCart(UUID cartId) {
        return Response.of(cartItemService.getMyCart(cartId));
    }

    @Override
    public Response<Boolean> deleteItemsFromMyCart(IdsRequest request) {
        cartItemService.deleteItemsFromMyCart(request);
        return Response.ok();
    }
}

package com.hhm.api.presentation.web.rest.impl;

import com.hhm.api.model.dto.request.AddCartRequest;
import com.hhm.api.model.dto.request.IdsRequest;
import com.hhm.api.model.dto.response.Response;
import com.hhm.api.model.entity.CartItem;
import com.hhm.api.presentation.web.rest.CartController;
import com.hhm.api.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CartControllerImpl implements CartController {
    private final CartService cartService;

    @Override
    public Response<Boolean> addItemToMyCart(AddCartRequest request) {
        cartService.addItemsToMyCart(request);
        return Response.ok();
    }

    @Override
    public Response<CartItem> getMyCart(UUID cartId) {
        return Response.of(cartService.getMyCart(cartId));
    }

    @Override
    public Response<Boolean> deleteItemsFromMyCart(IdsRequest request) {
        cartService.deleteItemsFromMyCart(request);
        return Response.ok();
    }
}

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

@RestController
@RequiredArgsConstructor
public class CartControllerImpl implements CartController {
    private final CartService cartService;

    @Override
    public Response<Boolean> addToMyCart(AddCartRequest request) {
        cartService.addToMyCart(request);
        return Response.ok();
    }

    @Override
    public Response<List<CartItem>> getMyCart() {
        return Response.of(cartService.getMyCart());
    }

    @Override
    public Response<Boolean> deleteFromMyCart(IdsRequest request) {
        cartService.deleteFromMyCart(request);
        return Response.ok();
    }
}

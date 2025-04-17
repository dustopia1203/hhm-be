package com.hhm.api.service;

import com.hhm.api.model.dto.request.CartItemRequest;
import com.hhm.api.model.dto.request.IdsRequest;
import com.hhm.api.model.dto.response.CartItemResponse;

import java.util.List;
import java.util.UUID;

public interface CartItemService {
    void addItemsToMyCart(CartItemRequest cartItemRequest);

    void deleteItemsFromMyCart(IdsRequest ids);

    List<CartItemResponse> getMyCart(UUID cartId);
}

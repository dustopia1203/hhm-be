package com.hhm.api.service;

import com.hhm.api.model.dto.request.CartItemRequest;
import com.hhm.api.model.dto.request.IdsRequest;
import com.hhm.api.model.entity.CartItem;

import java.util.List;
import java.util.UUID;

public interface CartService {
    void addItemsToMyCart(CartItemRequest request);

    void deleteItemsFromMyCart(IdsRequest request);

    List<CartItem> getMyCart(UUID cartId);
}

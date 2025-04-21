package com.hhm.api.service;

import com.hhm.api.model.dto.request.AddCartRequest;
import com.hhm.api.model.dto.request.IdsRequest;
import com.hhm.api.model.entity.CartItem;
import com.hhm.api.repository.CartItemRepository;

import java.util.List;
import java.util.UUID;

public interface CartService {
    void addItemsToMyCart(AddCartRequest request);

    void deleteItemsFromMyCart(IdsRequest request);

    CartItem getMyCart(UUID Id);
}

package com.hhm.api.service;

import com.hhm.api.model.dto.request.AddCartRequest;
import com.hhm.api.model.dto.request.IdsRequest;
import com.hhm.api.model.entity.CartItem;

import java.util.List;

public interface CartService {
    void addToMyCart(AddCartRequest request);

    List<CartItem> getMyCart();

    void deleteFromMyCart(IdsRequest request);
}

package com.hhm.api.service.impl;

import com.hhm.api.model.dto.request.AddCartRequest;
import com.hhm.api.model.dto.request.IdsRequest;
import com.hhm.api.model.entity.CartItem;
import com.hhm.api.repository.CartItemRepository;
import com.hhm.api.service.CartService;
import com.hhm.api.support.enums.error.NotFoundError;
import com.hhm.api.support.exception.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartItemRepository cartItemRepository;

    @Override
    public void addItemsToMyCart(AddCartRequest request) {
        Optional<CartItem> cartItemOptional = cartItemRepository.findByProduct(request.getProductId());

        if (cartItemOptional.isEmpty()) {
            CartItem cartItem = CartItem.builder()
                    .productId(request.getProductId())
                    .amount(request.getAmount())
                    .deleted(Boolean.FALSE)
                    .build();

            cartItemRepository.save(cartItem);

            return;
        }

        CartItem cartItem = cartItemOptional.get();

        cartItem.setAmount(cartItem.getAmount() + request.getAmount());

        cartItemRepository.save(cartItem);
    }

    @Override
    public void deleteItemsFromMyCart(IdsRequest request) {
        List<CartItem> cartItems = cartItemRepository.findByIds(request.getIds());

        request.getIds().forEach(id -> {
            Optional<CartItem> cartItemOptional = cartItems.stream()
                    .filter(cartItem -> Objects.equals(cartItem.getId(), id))
                    .findFirst();

            if (cartItemOptional.isEmpty()) {
                throw new ResponseException(NotFoundError.CART_ITEM_NOT_FOUND);
            }

            CartItem cartItem = cartItemOptional.get();

            cartItem.setDeleted(Boolean.TRUE);
        });

        cartItemRepository.saveAll(cartItems);
    }

    @Override
    public CartItem getMyCart(UUID Id) {

        return cartItemRepository.findById(Id).orElseThrow(
                () -> new ResponseException(NotFoundError.CART_ITEM_NOT_FOUND)
        );
    }
}

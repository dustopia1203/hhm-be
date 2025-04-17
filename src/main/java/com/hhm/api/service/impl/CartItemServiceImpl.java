package com.hhm.api.service.impl;

import com.hhm.api.model.dto.request.CartItemRequest;
import com.hhm.api.model.dto.request.IdsRequest;
import com.hhm.api.model.dto.response.CartItemResponse;
import com.hhm.api.model.entity.CartItem;
import com.hhm.api.model.entity.Product;
import com.hhm.api.repository.CartItemRepository;
import com.hhm.api.repository.ProductRepository;
import com.hhm.api.service.CartItemService;
import com.hhm.api.support.enums.error.NotFoundError;
import com.hhm.api.support.exception.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Override
    public void addItemsToMyCart(CartItemRequest request) {
        Optional<CartItem> cartItemOptional = cartItemRepository.findById(request.getProductId());

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
    public List<CartItemResponse> getMyCart(UUID cartId) {
        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);

        List<CartItemResponse> cartItemResponses = new ArrayList<>();

        cartItems.forEach(cartItem -> {
            Product product = productRepository.findById(cartItem.getProductId()).orElseThrow(
                    () -> new ResponseException(NotFoundError.PRODUCT_NOT_FOUND)
            );

            CartItemResponse cartItemResponse = CartItemResponse.builder()
                    .price(product.getPrice())
                    .description(product.getDescription())
                    .productName(product.getName())
                    .amount(product.getAmount())
                    .productId(product.getId())
                    .build();

            cartItemResponses.add(cartItemResponse);

        });

        return cartItemResponses;
    }
}

package com.hhm.api.service.impl;

import com.hhm.api.model.dto.mapper.AutoMapper;
import com.hhm.api.model.dto.request.AddCartRequest;
import com.hhm.api.model.dto.request.IdsRequest;
import com.hhm.api.model.dto.response.CartItemResponse;
import com.hhm.api.model.entity.CartItem;
import com.hhm.api.model.entity.Product;
import com.hhm.api.model.entity.Shop;
import com.hhm.api.repository.CartItemRepository;
import com.hhm.api.repository.ProductRepository;
import com.hhm.api.repository.ShopRepository;
import com.hhm.api.service.CartService;
import com.hhm.api.support.enums.error.NotFoundError;
import com.hhm.api.support.exception.ResponseException;
import com.hhm.api.support.util.IdUtils;
import com.hhm.api.support.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;
    private final AutoMapper autoMapper;

    @Override
    public void addToMyCart(AddCartRequest request) {
        UUID userId = SecurityUtils.getCurrentUserId();

        Optional<CartItem> cartItemOptional = cartItemRepository.findByProduct(request.getProductId());

        if (cartItemOptional.isEmpty()) {
            CartItem cartItem = CartItem.builder()
                    .id(IdUtils.nextId())
                    .userId(userId)
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
    public List<CartItemResponse> getMyCart() {
        UUID currentUserId = SecurityUtils.getCurrentUserId();

        List<CartItem> cartItems = cartItemRepository.findByUser(currentUserId);

        List<UUID> productIds = cartItems.stream()
                .map(CartItem::getProductId)
                .toList();

        List<Product> products = productRepository.findByIds(productIds);

        List<UUID> shopIds = products.stream()
                .map(Product::getShopId)
                .toList();

        List<Shop> shops = shopRepository.findByIds(shopIds);

        List<CartItemResponse> responses = new ArrayList<>();

        cartItems.forEach(cartItem -> {
            CartItemResponse response = autoMapper.toResponse(cartItem);

            Optional<Product> productOptional = products.stream()
                    .filter(product -> Objects.equals(product.getId(), cartItem.getProductId()))
                    .findFirst();

            if (productOptional.isEmpty()) return;

            Product product = productOptional.get();

            List<String> images = List.of(product.getContentUrls().split(";"));

            response.setName(product.getName());
            response.setImages(images);
            response.setPrice(product.getPrice());

            Optional<Shop> shopOptional = shops.stream()
                    .filter(shop -> Objects.equals(shop.getId(), product.getShopId()))
                    .findFirst();

            if (shopOptional.isEmpty()) return;

            Shop shop = shopOptional.get();

            response.setShopName(shop.getName());
            response.setLeftCount(0);

            responses.add(response);
        });

        return responses;
    }

    @Override
    public void deleteFromMyCart(IdsRequest request) {
        UUID userId = SecurityUtils.getCurrentUserId();

        List<CartItem> cartItems = cartItemRepository.findByIdsAndUser(request.getIds(), userId);

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
}

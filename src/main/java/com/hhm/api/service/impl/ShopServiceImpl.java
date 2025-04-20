package com.hhm.api.service.impl;

import com.hhm.api.model.dto.PageDTO;
import com.hhm.api.model.dto.mapper.AutoMapper;
import com.hhm.api.model.dto.request.IdsRequest;
import com.hhm.api.model.dto.request.ShopCreateOrUpdateRequest;
import com.hhm.api.model.dto.request.ShopSearchRequest;
import com.hhm.api.model.dto.response.ShopDetailResponse;
import com.hhm.api.model.entity.Shop;
import com.hhm.api.model.entity.projection.ReviewStat;
import com.hhm.api.repository.ProductRepository;
import com.hhm.api.repository.ReviewRepository;
import com.hhm.api.repository.ShopRepository;
import com.hhm.api.service.ShopService;
import com.hhm.api.support.enums.ActiveStatus;
import com.hhm.api.support.enums.error.AuthorizationError;
import com.hhm.api.support.enums.error.BadRequestError;
import com.hhm.api.support.enums.error.NotFoundError;
import com.hhm.api.support.exception.ResponseException;
import com.hhm.api.support.util.IdUtils;
import com.hhm.api.support.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShopServiceImpl implements ShopService {
    private final AutoMapper autoMapper;
    private final ShopRepository shopRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public PageDTO<Shop> search(ShopSearchRequest request) {
        Long count = shopRepository.count(request);

        if (Objects.equals(count, 0L)) {
            return PageDTO.empty(request.getPageIndex(), request.getPageSize());
        }

        List<Shop> shops = shopRepository.search(request);

        return PageDTO.of(shops, request.getPageIndex(), request.getPageSize(), count);
    }

    @Override
    public ShopDetailResponse getById(UUID id) {
        Optional<Shop> shopOptional = shopRepository.findById(id);

        if (shopOptional.isEmpty()) {
            throw new ResponseException(NotFoundError.SHOP_NOT_FOUND);
        }

        Shop shop = shopOptional.get();

        return getShopDetailResponse(shop);
    }

    @Override
    public ShopDetailResponse getMy() {
        UUID userId = SecurityUtils.getCurrentUserId();

        Optional<Shop> shopOptional = shopRepository.findByUser(userId);

        if (shopOptional.isEmpty()) {
            throw new ResponseException(NotFoundError.SHOP_NOT_FOUND);
        }

        Shop shop = shopOptional.get();

        return getShopDetailResponse(shop);
    }

    @Override
    public Shop createMy(ShopCreateOrUpdateRequest request) {
        UUID userId = SecurityUtils.getCurrentUserId();

        Optional<Shop> shopOptional = shopRepository.findByUser(userId);

        if (shopOptional.isPresent()) {
            throw new ResponseException(BadRequestError.USER_ALREADY_HAS_SHOP);
        }

        Shop shop = Shop.builder()
                .id(IdUtils.nextId())
                .userId(userId)
                .name(request.getName())
                .address(request.getAddress())
                .avatarUrl(request.getAvatarUrl())
                .status(ActiveStatus.ACTIVE)
                .deleted(Boolean.FALSE)
                .build();

        shopRepository.save(shop);

        return shop;
    }

    @Override
    public Shop updateMy(ShopCreateOrUpdateRequest request) {
        UUID userId = SecurityUtils.getCurrentUserId();

        Optional<Shop> shopOptional = shopRepository.findByUser(userId);

        if (shopOptional.isEmpty()) {
            throw new ResponseException(NotFoundError.SHOP_NOT_FOUND);
        }

        Shop shop = shopOptional.get();

        shop.setName(request.getName());
        shop.setAddress(request.getAddress());
        shop.setAvatarUrl(request.getAvatarUrl());

        shopRepository.save(shop);

        return shop;
    }

    @Override
    public void active(IdsRequest request) {
        List<Shop> shops = shopRepository.findByIds(request.getIds());

        request.getIds().forEach(id -> {
            Optional<Shop> shopOptional = shops.stream()
                    .filter(shop -> Objects.equals(shop.getId(), id))
                    .findFirst();

            if (shopOptional.isEmpty()) {
                throw new ResponseException(NotFoundError.SHOP_NOT_FOUND);
            }

            Shop shop = shopOptional.get();

            if (Objects.equals(shop.getStatus(), ActiveStatus.ACTIVE)) {
                throw new ResponseException(BadRequestError.SHOP_WAS_ACTIVATED);
            }

            shop.setStatus(ActiveStatus.ACTIVE);
        });

        shopRepository.saveAll(shops);
    }

    @Override
    public void inactive(IdsRequest request) {
        List<Shop> shops = shopRepository.findByIds(request.getIds());

        request.getIds().forEach(id -> {
            Optional<Shop> shopOptional = shops.stream()
                    .filter(shop -> Objects.equals(shop.getId(), id))
                    .findFirst();

            if (shopOptional.isEmpty()) {
                throw new ResponseException(NotFoundError.SHOP_NOT_FOUND);
            }

            Shop shop = shopOptional.get();

            if (Objects.equals(shop.getStatus(), ActiveStatus.INACTIVE)) {
                throw new ResponseException(BadRequestError.SHOP_WAS_INACTIVATED);
            }

            shop.setStatus(ActiveStatus.INACTIVE);
        });

        shopRepository.saveAll(shops);
    }

    @Override
    public void delete(IdsRequest request) {
        List<Shop> shops = shopRepository.findByIds(request.getIds());

        request.getIds().forEach(id -> {
            Optional<Shop> shopOptional = shops.stream()
                    .filter(shop -> Objects.equals(shop.getId(), id))
                    .findFirst();

            if (shopOptional.isEmpty()) {
                throw new ResponseException(NotFoundError.SHOP_NOT_FOUND);
            }

            Shop shop = shopOptional.get();

            shop.setDeleted(Boolean.TRUE);
        });

        shopRepository.saveAll(shops);
    }

    private ShopDetailResponse getShopDetailResponse(Shop shop) {
        ShopDetailResponse response = autoMapper.toResponse(shop);

        Long productCount = productRepository.countByShop(shop.getId());

        ReviewStat reviewCount = reviewRepository.findStatByShop(shop.getId());

        response.setProductCount(productCount);
        response.setReviewCount(Objects.nonNull(reviewCount.getReviewCount()) ? reviewCount.getReviewCount() : 0L);
        response.setRating(Objects.nonNull(reviewCount.getAvgRating()) ? reviewCount.getAvgRating() : 0);

        return response;
    }
}

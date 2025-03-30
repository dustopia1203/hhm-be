package com.hhm.api.service;

import com.hhm.api.model.dto.PageDTO;
import com.hhm.api.model.dto.request.IdsRequest;
import com.hhm.api.model.dto.request.ShopCreateOrUpdateRequest;
import com.hhm.api.model.dto.request.ShopSearchRequest;
import com.hhm.api.model.entity.Shop;

import java.util.UUID;

public interface ShopService {
    PageDTO<Shop> search(ShopSearchRequest request);

    Shop getById(UUID id);

    Shop create(ShopCreateOrUpdateRequest request);

    Shop update(UUID id, ShopCreateOrUpdateRequest request);

    void active(IdsRequest request);

    void inactive(IdsRequest request);

    void delete(IdsRequest request);
}

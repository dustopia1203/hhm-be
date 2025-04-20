package com.hhm.api.service;

import com.hhm.api.model.dto.PageDTO;
import com.hhm.api.model.dto.request.IdsRequest;
import com.hhm.api.model.dto.request.ShopCreateOrUpdateRequest;
import com.hhm.api.model.dto.request.ShopSearchRequest;
import com.hhm.api.model.dto.response.ShopDetailResponse;
import com.hhm.api.model.entity.Shop;

import java.util.UUID;

public interface ShopService {
    PageDTO<Shop> search(ShopSearchRequest request);

    ShopDetailResponse getById(UUID id);

    ShopDetailResponse getMy();

    Shop createMy(ShopCreateOrUpdateRequest request);

    Shop updateMy(ShopCreateOrUpdateRequest request);

    void active(IdsRequest request);

    void inactive(IdsRequest request);

    void delete(IdsRequest request);
}

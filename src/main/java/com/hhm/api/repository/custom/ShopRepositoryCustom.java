package com.hhm.api.repository.custom;

import com.hhm.api.model.dto.request.ShopSearchRequest;
import com.hhm.api.model.entity.Shop;

import java.util.List;

public interface ShopRepositoryCustom {
    Long count(ShopSearchRequest request);

    List<Shop> search(ShopSearchRequest request);
}

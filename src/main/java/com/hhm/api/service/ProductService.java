package com.hhm.api.service;

import com.hhm.api.model.dto.PageDTO;
import com.hhm.api.model.dto.request.IdsRequest;
import com.hhm.api.model.dto.request.ProductCreateOrUpdateRequest;
import com.hhm.api.model.dto.request.ProductSearchRequest;
import com.hhm.api.model.dto.response.ProductResponse;
import com.hhm.api.model.entity.Product;

import java.util.UUID;

public interface ProductService {
    PageDTO<ProductResponse> search(ProductSearchRequest request);

    ProductResponse getById(UUID id);

    Product createMyShop(ProductCreateOrUpdateRequest request);

    Product updateMyShop(UUID id, ProductCreateOrUpdateRequest request);

    void activeMyShop(IdsRequest request);

    void inactiveMyShop(IdsRequest request);

    void deleteMyShop(IdsRequest request);
}

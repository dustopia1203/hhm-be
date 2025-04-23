package com.hhm.api.service;

import com.hhm.api.model.dto.PageDTO;
import com.hhm.api.model.dto.request.IdsRequest;
import com.hhm.api.model.dto.request.ProductCreateOrUpdateRequest;
import com.hhm.api.model.dto.request.ProductSearchRequest;
import com.hhm.api.model.dto.response.ProductResponse;
import com.hhm.api.model.entity.Product;

import java.util.UUID;

public interface ProductService {
    PageDTO<Product> search(ProductSearchRequest request);

    ProductResponse getById(UUID id);

    Product create(ProductCreateOrUpdateRequest request);

    Product update(UUID id, ProductCreateOrUpdateRequest request);

    void active(IdsRequest request);

    void inactive(IdsRequest request);

    void delete(IdsRequest request);
}

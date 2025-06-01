package com.hhm.api.repository.custom;

import com.hhm.api.model.dto.request.ProductSearchRequest;
import com.hhm.api.model.entity.Product;

import java.util.List;

public interface ProductRepositoryCustom {
    Long count(ProductSearchRequest request);

    List<Product> search(ProductSearchRequest request);
}

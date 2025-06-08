package com.hhm.api.presentation.web.rest.impl;

import com.hhm.api.model.dto.request.IdsRequest;
import com.hhm.api.model.dto.request.ProductCreateOrUpdateRequest;
import com.hhm.api.model.dto.request.ProductSearchRequest;
import com.hhm.api.model.dto.response.PagingResponse;
import com.hhm.api.model.dto.response.ProductResponse;
import com.hhm.api.model.dto.response.Response;
import com.hhm.api.model.entity.Product;
import com.hhm.api.presentation.web.rest.ProductController;
import com.hhm.api.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ProductControllerImpl implements ProductController {
    private final ProductService productService;

    @Override
    public PagingResponse<ProductResponse> search(ProductSearchRequest request) {
        return PagingResponse.of(productService.search(request));
    }

    @Override
    public Response<ProductResponse> getById(UUID id) {
        return Response.of(productService.getById(id));
    }

    @Override
    public Response<Product> createMyShop(ProductCreateOrUpdateRequest request) {
        return Response.of(productService.createMyShop(request));
    }

    @Override
    public Response<Product> updateMyShop(UUID id, ProductCreateOrUpdateRequest request) {
        return Response.of(productService.updateMyShop(id, request));
    }

    @Override
    public Response<Boolean> activeMyShop(IdsRequest request) {
        productService.activeMyShop(request);
        return Response.ok();
    }

    @Override
    public Response<Boolean> inactiveMyShop(IdsRequest request) {
        productService.inactiveMyShop(request);
        return Response.ok();
    }

    @Override
    public Response<Boolean> deleteMyShop(IdsRequest request) {
        productService.deleteMyShop(request);
        return Response.ok();
    }
}

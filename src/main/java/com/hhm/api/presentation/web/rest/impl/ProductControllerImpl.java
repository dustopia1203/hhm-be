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
    public PagingResponse<Product> search(ProductSearchRequest request) {
        return PagingResponse.of(productService.search(request));
    }

    @Override
    public Response<ProductResponse> getById(UUID id) {
        return Response.of(productService.getById(id));
    }

    @Override
    public Response<Product> create(ProductCreateOrUpdateRequest request) {
        return Response.of(productService.create(request));
    }

    @Override
    public Response<Product> update(UUID id, ProductCreateOrUpdateRequest request) {
        return Response.of(productService.update(id, request));
    }

    @Override
    public Response<Boolean> active(IdsRequest request) {
        productService.active(request);
        return Response.ok();
    }

    @Override
    public Response<Boolean> inactive(IdsRequest request) {
        productService.inactive(request);
        return Response.ok();
    }

    @Override
    public Response<Boolean> delete(IdsRequest request) {
        productService.delete(request);
        return Response.ok();
    }
}

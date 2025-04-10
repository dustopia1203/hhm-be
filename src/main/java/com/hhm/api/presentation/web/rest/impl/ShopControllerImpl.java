package com.hhm.api.presentation.web.rest.impl;

import com.hhm.api.model.dto.request.IdsRequest;
import com.hhm.api.model.dto.request.ShopCreateOrUpdateRequest;
import com.hhm.api.model.dto.request.ShopSearchRequest;
import com.hhm.api.model.dto.response.PagingResponse;
import com.hhm.api.model.dto.response.Response;
import com.hhm.api.model.entity.Shop;
import com.hhm.api.presentation.web.rest.ShopController;
import com.hhm.api.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ShopControllerImpl implements ShopController {
    private final ShopService shopService;

    @Override
    public PagingResponse<Shop> search(ShopSearchRequest request) {
        return PagingResponse.of(shopService.search(request));
    }

    @Override
    public Response<Shop> getById(UUID id) {
        return Response.of(shopService.getById(id));
    }

    @Override
    public Response<Shop> create(ShopCreateOrUpdateRequest request) {
        return Response.of(shopService.create(request));
    }

    @Override
    public Response<Shop> update(UUID id, ShopCreateOrUpdateRequest request) {
        return Response.of(shopService.update(id, request));
    }

    @Override
    public Response<Boolean> active(IdsRequest request) {
        shopService.active(request);
        return Response.ok();
    }

    @Override
    public Response<Boolean> inactive(IdsRequest request) {
        shopService.inactive(request);
        return Response.ok();
    }

    @Override
    public Response<Boolean> delete(IdsRequest request) {
        shopService.delete(request);
        return Response.ok();
    }
}

package com.hhm.api.presentation.web.rest.impl;

import com.hhm.api.model.dto.request.IdsRequest;
import com.hhm.api.model.dto.request.ShopCreateOrUpdateRequest;
import com.hhm.api.model.dto.request.ShopSearchRequest;
import com.hhm.api.model.dto.response.PagingResponse;
import com.hhm.api.model.dto.response.Response;
import com.hhm.api.model.dto.response.ShopDetailResponse;
import com.hhm.api.model.entity.Refund;
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
    public Response<ShopDetailResponse> getById(UUID id) {
        return Response.of(shopService.getById(id));
    }

    @Override
    public Response<ShopDetailResponse> getMy() {
        return Response.of(shopService.getMy());
    }

    @Override
    public Response<Shop> createMy(ShopCreateOrUpdateRequest request) {
        return Response.of(shopService.createMy(request));
    }

    @Override
    public Response<Shop> updateMy(ShopCreateOrUpdateRequest request) {
        return Response.of(shopService.updateMy(request));
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

    @Override
    public Response<Boolean> confirmMyShopOrder(UUID orderId) {
        shopService.confirmMyShopOrder(orderId);
        return Response.ok();
    }

    @Override
    public Response<Refund> getMyShopRefund(UUID orderId) {
        return Response.of(shopService.getMyShopRefund(orderId));
    }

    @Override
    public Response<Boolean> confirmMyShopRefund(UUID orderId) {
        shopService.confirmMyShopRefund(orderId);
        return Response.ok();
    }
}

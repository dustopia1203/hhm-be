package com.hhm.api.presentation.web.rest.impl;

import com.hhm.api.model.dto.request.ShippingCreateOrUpdateRequest;
import com.hhm.api.model.dto.request.IdsRequest;
import com.hhm.api.model.dto.request.ShippingSearchRequest;
import com.hhm.api.model.dto.response.PagingResponse;
import com.hhm.api.model.dto.response.Response;
import com.hhm.api.model.entity.Shipping;
import com.hhm.api.presentation.web.rest.ShippingController;
import com.hhm.api.service.ShippingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ShippingControllerImpl implements ShippingController {
    private final ShippingService shippingService;

    @Override
    public PagingResponse<Shipping> search(ShippingSearchRequest request) {
        return PagingResponse.of(shippingService.search(request));
    }

    @Override
    public Response<Shipping> create(ShippingCreateOrUpdateRequest request) {
        return Response.of(shippingService.create(request));
    }

    @Override
    public Response<Shipping> update(UUID id, ShippingCreateOrUpdateRequest request) {
        return Response.of(shippingService.update(id, request));
    }

    @Override
    public Response<Boolean> active(IdsRequest request) {
        shippingService.active(request);
        return Response.ok();
    }

    @Override
    public Response<Boolean> inactive(IdsRequest request) {
        shippingService.inactive(request);
        return Response.ok();
    }

    @Override
    public Response<Boolean> delete(IdsRequest request) {
        shippingService.delete(request);
        return Response.ok();
    }
}

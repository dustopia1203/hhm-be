package com.hhm.api.service;

import com.hhm.api.model.dto.PageDTO;
import com.hhm.api.model.dto.request.IdsRequest;
import com.hhm.api.model.dto.request.ShippingCreateOrUpdateRequest;
import com.hhm.api.model.dto.request.ShippingSearchRequest;
import com.hhm.api.model.entity.Shipping;

import java.util.UUID;

public interface ShippingService {
    PageDTO<Shipping> search(ShippingSearchRequest request);

    Shipping create(ShippingCreateOrUpdateRequest request);

    Shipping update(UUID id, ShippingCreateOrUpdateRequest request);

    void active(IdsRequest request);

    void inactive(IdsRequest request);

    void delete(IdsRequest request);
}

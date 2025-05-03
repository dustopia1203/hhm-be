package com.hhm.api.repository.custom;

import com.hhm.api.model.dto.request.ShippingSearchRequest;
import com.hhm.api.model.entity.Shipping;

import java.util.List;

public interface ShippingRepositoryCustom {
    Long count(ShippingSearchRequest request);

    List<Shipping> search(ShippingSearchRequest request);
}

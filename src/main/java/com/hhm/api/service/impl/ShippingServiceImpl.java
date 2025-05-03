package com.hhm.api.service.impl;

import com.hhm.api.model.dto.PageDTO;
import com.hhm.api.model.dto.request.IdsRequest;
import com.hhm.api.model.dto.request.ShippingCreateOrUpdateRequest;
import com.hhm.api.model.dto.request.ShippingSearchRequest;
import com.hhm.api.model.entity.Shipping;
import com.hhm.api.repository.ShippingRepository;
import com.hhm.api.service.ShippingService;
import com.hhm.api.support.enums.ActiveStatus;
import com.hhm.api.support.enums.error.BadRequestError;
import com.hhm.api.support.enums.error.NotFoundError;
import com.hhm.api.support.exception.ResponseException;
import com.hhm.api.support.util.IdUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShippingServiceImpl implements ShippingService {
    private final ShippingRepository shippingRepository;

    @Override
    public PageDTO<Shipping> search(ShippingSearchRequest request) {
        Long count = shippingRepository.count(request);

        if (Objects.equals(count, 0L)) {
            return PageDTO.empty(request.getPageIndex(), request.getPageSize());
        }

        List<Shipping> shippings = shippingRepository.search(request);

        return PageDTO.of(shippings, request.getPageIndex(), request.getPageSize(), count);
    }

    @Override
    public Shipping create(ShippingCreateOrUpdateRequest request) {
        Shipping shipping = Shipping.builder()
                .id(IdUtils.nextId())
                .name(request.getName())
                .avatarUrl(request.getAvatarUrl())
                .duration(request.getDuration())
                .price(request.getPrice())
                .deleted(Boolean.FALSE)
                .status(ActiveStatus.ACTIVE)
                .build();

        shippingRepository.save(shipping);

        return shipping;
    }

    @Override
    public Shipping update(UUID id, ShippingCreateOrUpdateRequest request) {
        Optional<Shipping> shippingOptional = shippingRepository.findById(id);

        if (shippingOptional.isEmpty()) {
            throw new ResponseException(NotFoundError.SHIPPING_NOT_FOUND);
        }

        Shipping shipping = shippingOptional.get();

        shipping.setName(request.getName());
        shipping.setAvatarUrl(request.getAvatarUrl());
        shipping.setDuration(request.getDuration());
        shipping.setPrice(request.getPrice());

        shippingRepository.save(shipping);

        return shipping;
    }

    @Override
    public void active(IdsRequest request) {
        List<Shipping> shippings = shippingRepository.findByIds(request.getIds());

        request.getIds().forEach(id -> {
            Optional<Shipping> shippingOptional = shippings.stream()
                    .filter(shipping -> Objects.equals(shipping.getId(), id))
                    .findFirst();

            if (shippingOptional.isEmpty()) {
                throw new ResponseException(NotFoundError.SHIPPING_NOT_FOUND);
            }

            Shipping shipping = shippingOptional.get();

            if (Objects.equals(shipping.getStatus(), ActiveStatus.ACTIVE)) {
                throw new ResponseException(BadRequestError.SHIPPING_WAS_ACTIVATED);
            }

            shipping.setStatus(ActiveStatus.ACTIVE);
        });

        shippingRepository.saveAll(shippings);
    }

    @Override
    public void inactive(IdsRequest request) {
        List<Shipping> shippings = shippingRepository.findByIds(request.getIds());

        request.getIds().forEach(id -> {
            Optional<Shipping> shippingOptional = shippings.stream()
                    .filter(shipping -> Objects.equals(shipping.getId(), id))
                    .findFirst();

            if (shippingOptional.isEmpty()) {
                throw new ResponseException(NotFoundError.SHIPPING_NOT_FOUND);
            }

            Shipping shipping = shippingOptional.get();

            if (Objects.equals(shipping.getStatus(), ActiveStatus.INACTIVE)) {
                throw new ResponseException(BadRequestError.SHIPPING_WAS_INACTIVATED);
            }

            shipping.setStatus(ActiveStatus.INACTIVE);
        });

        shippingRepository.saveAll(shippings);
    }

    @Override
    public void delete(IdsRequest request) {
        List<Shipping> shippings = shippingRepository.findByIds(request.getIds());

        request.getIds().forEach(id -> {
            Optional<Shipping> shippingOptional = shippings.stream()
                    .filter(shipping -> Objects.equals(shipping.getId(), id))
                    .findFirst();

            if (shippingOptional.isEmpty()) {
                throw new ResponseException(NotFoundError.SHIPPING_NOT_FOUND);
            }

            Shipping shipping = shippingOptional.get();

            shipping.setDeleted(Boolean.TRUE);
        });

        shippingRepository.saveAll(shippings);
    }
}

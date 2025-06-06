package com.hhm.api.service;

import com.hhm.api.model.dto.PageDTO;
import com.hhm.api.model.dto.request.ReviewCreateRequest;
import com.hhm.api.model.dto.request.ReviewSearchRequest;
import com.hhm.api.model.dto.response.ReviewResponse;
import com.hhm.api.model.entity.Review;

import java.util.UUID;

public interface ReviewService {
    PageDTO<ReviewResponse> search(ReviewSearchRequest request);

    Review createMy(ReviewCreateRequest request);

    PageDTO<ReviewResponse> findAllByShopId(UUID shopId);
}

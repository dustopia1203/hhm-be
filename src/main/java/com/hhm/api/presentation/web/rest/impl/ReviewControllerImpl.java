package com.hhm.api.presentation.web.rest.impl;

import com.hhm.api.model.dto.request.ReviewCreateRequest;
import com.hhm.api.model.dto.request.ReviewSearchRequest;
import com.hhm.api.model.dto.response.PagingResponse;
import com.hhm.api.model.dto.response.Response;
import com.hhm.api.model.dto.response.ReviewResponse;
import com.hhm.api.model.entity.Review;
import com.hhm.api.presentation.web.rest.ReviewController;
import com.hhm.api.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReviewControllerImpl implements ReviewController {
    private final ReviewService reviewService;

    @Override
    public PagingResponse<ReviewResponse> search(ReviewSearchRequest request) {
        return PagingResponse.of(reviewService.search(request));
    }

    @Override
    public Response<Review> createMy(ReviewCreateRequest request) {
        return Response.of(reviewService.createMy(request));
    }
}

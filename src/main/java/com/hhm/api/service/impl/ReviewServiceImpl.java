package com.hhm.api.service.impl;

import com.hhm.api.model.dto.PageDTO;
import com.hhm.api.model.dto.request.ReviewCreateRequest;
import com.hhm.api.model.dto.request.ReviewSearchRequest;
import com.hhm.api.model.entity.Review;
import com.hhm.api.repository.ReviewRepository;
import com.hhm.api.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;

    @Override
    public PageDTO<Review> search(ReviewSearchRequest request) {
        return PageDTO.empty(request.getPageIndex(), request.getPageSize());
    }

    @Override
    public Review createMy(ReviewCreateRequest request) {
        return null;
    }
}

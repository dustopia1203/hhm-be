package com.hhm.api.repository.custom;

import com.hhm.api.model.dto.request.ReviewSearchRequest;
import com.hhm.api.model.entity.Review;

import java.util.List;

public interface ReviewRepositoryCustom {
    Long count(ReviewSearchRequest request);

    List<Review> search(ReviewSearchRequest request);
}

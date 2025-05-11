package com.hhm.api.presentation.web.rest;

import com.hhm.api.config.application.validator.ValidatePaging;
import com.hhm.api.model.dto.request.ReviewCreateRequest;
import com.hhm.api.model.dto.request.ReviewSearchRequest;
import com.hhm.api.model.dto.response.PagingResponse;
import com.hhm.api.model.dto.response.Response;
import com.hhm.api.model.entity.Review;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Review Resources")
@RequestMapping("/api/reviews")
@Validated
public interface ReviewController {
    @Operation(summary = "Search reviews")
    @GetMapping("/q")
    PagingResponse<Review> search(@ValidatePaging(sortModel = Review.class) ReviewSearchRequest request);

    @Operation(summary = "Create my review")
    @PostMapping("/my")
    Response<Review> createMy(@Valid @RequestBody ReviewCreateRequest request);
}

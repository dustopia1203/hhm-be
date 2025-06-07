package com.hhm.api.presentation.web.rest.impl;

import com.hhm.api.model.elasticsearch.ProductDocument;
import com.hhm.api.model.elasticsearch.UserBehaviorDocument;
import com.hhm.api.presentation.web.rest.ProductRecommendationController;
import com.hhm.api.service.ProductRecommendationService;
import com.hhm.api.support.exception.ResponseException;
import com.hhm.api.support.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ProductRecommendationControllerImpl implements ProductRecommendationController {
    private final ProductRecommendationService productRecommendationService;

    @Override
    public List<ProductDocument> getSimilarProducts(UUID productId, int size) {
        return productRecommendationService.getSimilarProducts(productId, size);
    }

    @Override
    public void trackBehavior(UserBehaviorDocument behavior) {
        behavior.setUserId(SecurityUtils.getCurrentUserId());
        productRecommendationService.trackUserBehavior(behavior);
    }

    @Override
    public List<ProductDocument> getSimilarProductsFromSearches(int size) {
        try {
            UUID userId = SecurityUtils.getCurrentUserId();
            return productRecommendationService.getSimilarProductsFromRecentSearches(userId, size);
        } catch (ResponseException e) {
            // Nếu không lấy được userId (user chưa đăng nhập), truyền null
            return productRecommendationService.getSimilarProductsFromRecentSearches(null, size);
        }
    }

    @Override
    public List<ProductDocument> suggest(String query) {
        return productRecommendationService.suggest(query);
    }

} 
package com.hhm.api.service;

import com.hhm.api.model.elasticsearch.ProductDocument;
import com.hhm.api.model.elasticsearch.UserBehaviorDocument;

import java.util.List;
import java.util.UUID;

public interface ProductRecommendationService {
    void trackUserBehavior(UserBehaviorDocument behavior);
    List<ProductDocument> getSimilarProducts(UUID productId, int size);
    List<ProductDocument> getSimilarProductsFromRecentSearches(UUID userId, int size);
    List<String> getTopSearchQueriesWithoutUserId();
    List<ProductDocument> suggest(String query);
} 
package com.hhm.api.presentation.web.rest;

import com.hhm.api.model.elasticsearch.ProductDocument;
import com.hhm.api.model.elasticsearch.UserBehaviorDocument;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Product Recommendation API")
public interface ProductRecommendationController {
    @Operation(summary = "Get personalized product recommendations")
    @GetMapping("/api/v1/products/recommendations")
    List<ProductDocument> getRecommendations(
        @RequestParam(defaultValue = "10") int size
    );

    @Operation(summary = "Get similar products")
    @GetMapping("/api/v1/products/{productId}/similar")
    List<ProductDocument> getSimilarProducts(
        @PathVariable UUID productId,
        @RequestParam(defaultValue = "10") int size
    );

    @Operation(summary = "Track user behavior")
    @PostMapping("/api/v1/products/track-behavior")
    void trackBehavior(@RequestBody UserBehaviorDocument behavior);

    @Operation(summary = "Get similar products based on recent searches")
    @GetMapping("/api/v1/products/similar-from-searches")
    List<ProductDocument> getSimilarProductsFromSearches(
        @RequestParam(defaultValue = "10") int size
    );
    
    @Operation(summary = "Get suggest products")
    @GetMapping("/api/v1/products/suggest")
    List<ProductDocument> suggest(@RequestParam String query);


} 
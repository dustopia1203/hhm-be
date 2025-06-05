package com.hhm.api.service.impl;

import com.hhm.api.model.elasticsearch.ProductDocument;
import com.hhm.api.model.elasticsearch.UserBehaviorDocument;
import com.hhm.api.repository.elasticsearch.ProductElasticsearchRepository;
import com.hhm.api.repository.elasticsearch.UserBehaviorElasticsearchRepository;
import com.hhm.api.service.ProductRecommendationService;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.util.NamedValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductRecommendationServiceImpl implements ProductRecommendationService {
    private final ProductElasticsearchRepository productElasticsearchRepository;
    private final UserBehaviorElasticsearchRepository userBehaviorElasticsearchRepository;
    private final ElasticsearchClient elasticsearchClient;

    @Override
    public List<ProductDocument> suggest(String query) {
        return productElasticsearchRepository.suggestByNameOrDescription(query);
    }

    @Override
    public List<String> getTopSearchQueriesWithoutUserId() {
        try {
            SearchResponse<Void> response = elasticsearchClient.search(s -> s
                .index("user_behaviors")
                .size(0)
                .query(q -> q
                    .bool(b -> b
                        .must(m -> m.term(t -> t.field("behaviorType").value("SEARCH")))
                        .must(m -> m.exists(e -> e.field("searchQuery")))
                    )
                )
                .aggregations("top_keywords", a -> a
                    .terms(t -> t
                        .field("searchQuery.keyword")
                        .size(5)
                        .order(List.of(new NamedValue<>("_count", SortOrder.Desc)))
                    )
                ), 
                Void.class
            );

            return response.aggregations().get("top_keywords").sterms().buckets().array().stream()
                .map(bucket -> bucket.key().stringValue())
                .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("Error getting top search queries without userId", e);
            return Collections.emptyList();
        }
    }

    @Override
    public void trackUserBehavior(UserBehaviorDocument behavior) {
        behavior.setId(UUID.randomUUID().toString());
        behavior.setTimestamp(Instant.now());
        userBehaviorElasticsearchRepository.save(behavior);
    }

    @Override
    public List<ProductDocument> getSimilarProducts(UUID productId, int size) {
        Optional<ProductDocument> productOpt = productElasticsearchRepository.findById(productId);
        if (productOpt.isEmpty()) {
            log.warn("Product not found with id: {}", productId);
            return Collections.emptyList();
        }

        ProductDocument product = productOpt.get();
        List<String> categoryIds = product.getCategoryId() != null
                ? Collections.singletonList(product.getCategoryId().toString())
                : Collections.emptyList();

        List<String> shopIds = product.getShopId() != null ? Collections.singletonList(product.getShopId().toString())
                : Collections.emptyList();

        log.info("Finding similar products for product: {}, categoryIds: {}, shopIds: {}, size: {}",
                productId, categoryIds, shopIds, size);

        List<ProductDocument> similarProducts = productElasticsearchRepository.findSimilarProducts(
                categoryIds,
                shopIds,
                Collections.singletonList(productId),
                size);

        log.info("Found {} similar products", similarProducts.size());
        return similarProducts;
    }

    @Override
    public List<ProductDocument> getSimilarProductsFromRecentSearches(UUID userId, int size) {
        log.info("Getting similar products from recent searches for user: {}", userId);

        List<String> searchQueries;
        if (userId != null) {
            // Lấy 5 từ khóa tìm kiếm gần nhất của user cụ thể
            Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Order.desc("timestamp")));
            List<UserBehaviorDocument> recentSearches = userBehaviorElasticsearchRepository.findRecentSearches(userId, pageable);
            searchQueries = recentSearches.stream()
                .map(UserBehaviorDocument::getSearchQuery)
                .collect(Collectors.toList());
        } else {
            // Lấy 5 từ khóa tìm kiếm phổ biến nhất trong 7 ngày gần đây
            searchQueries = getTopSearchQueriesWithoutUserId();
            log.info("searchQueries k userid: {}", searchQueries);
        }
    
        if (searchQueries.isEmpty()) {
            log.info("No search queries found");
            return Collections.emptyList();
        }
    
        int productsPerSearch = Math.max(1, size / searchQueries.size());
        Set<UUID> recommendedIds = new HashSet<>();
        List<ProductDocument> recommendations = new ArrayList<>();
    
        // Tìm sản phẩm tương tự cho mỗi từ khóa
        for (String query : searchQueries) {
            if (recommendations.size() >= size)
                break;
    
            log.info("Finding products for search query: {}", query);
            List<ProductDocument> searchResults = productElasticsearchRepository.suggestByNameOrDescription(query);
            List<ProductDocument> limitedResults = searchResults.stream()
                .filter(product -> !recommendedIds.contains(product.getId()))
                .limit(productsPerSearch)
                .collect(Collectors.toList());
    
            recommendations.addAll(limitedResults);
            recommendedIds.addAll(limitedResults.stream()
                    .map(ProductDocument::getId)
                    .collect(Collectors.toSet()));
        }
    
        log.info("Found {} similar products from recent searches", recommendations.size());
        return recommendations;
    }

}
package com.hhm.api.service.impl;

import com.hhm.api.model.elasticsearch.ProductDocument;
import com.hhm.api.model.elasticsearch.UserBehaviorDocument;
import com.hhm.api.repository.elasticsearch.ProductElasticsearchRepository;
import com.hhm.api.repository.elasticsearch.UserBehaviorElasticsearchRepository;
import com.hhm.api.service.ProductRecommendationService;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import co.elastic.clients.util.NamedValue;
import co.elastic.clients.elasticsearch._types.SortOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

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
                                    .must(m -> m.exists(e -> e.field("searchQuery")))))
                    .aggregations("top_keywords", a -> a
                            .terms(t -> t
                                    .field("searchQuery.keyword")
                                    .size(5)
                                    .order(List.of(new NamedValue<>("_count", SortOrder.Desc))))),
                    Void.class);

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

    private List<String> analyzeText(String text) {
        try {
            var response = elasticsearchClient.indices().analyze(a -> a
                    .index("products")
                    .field("name.folded")
                    .text(text));
            
            System.out.println("response = " + response);
            System.out.println("response.tokens() = " + response.tokens().stream()
                    .map(token -> token.token())
                    .collect(Collectors.toList()));
            return response.tokens().stream()
                    .map(token -> token.token())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("Error analyzing text: {}", text, e);
            return Collections.emptyList();
        }
    }

    public List<ProductDocument> similarProductsBasedOnCategoryAndShop(String categoryId, String shopId, int size) throws IOException {
        var boolQuery = new co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery.Builder();
        
        if (categoryId != null) {
            boolQuery.should(s -> s
                .term(t -> t
                    .field("category_id.keyword")
                    .value(categoryId)
                    .boost(2.0f)
                )
            );
        }
        
        if (shopId != null) {
            boolQuery.should(s -> s
                .term(t -> t
                    .field("shop_id.keyword")
                    .value(shopId)
                    .boost(1.5f)
                )
            );
        }
        
        boolQuery.minimumShouldMatch("1");

        SearchResponse<JsonData> response = elasticsearchClient.search(s -> s
                .index("products")
                .query(q -> q.bool(boolQuery.build()))
                .sort(sort -> sort
                    .score(sc -> sc.order(SortOrder.Desc))
                )
                .size(size),
                JsonData.class
        );

        ObjectMapper mapper = new ObjectMapper();
        return response.hits().hits().stream()
            .map(hit -> {
                JsonData source = hit.source();
                if (source == null) return null;
                
                try {
                    // Lấy JSON string từ JsonData
                    String jsonString = source.toJson().toString();
                    JsonNode json = mapper.readTree(jsonString);
                    
                    return ProductDocument.builder()
                        .id(UUID.fromString(json.get("id").asText()))
                        .shopId(json.has("shop_id") ? 
                            UUID.fromString(json.get("shop_id").asText()) : null)
                        .categoryId(json.has("category_id") ? 
                            UUID.fromString(json.get("category_id").asText()) : null)
                        .name(json.has("name") ? json.get("name").asText() : null)
                        .description(json.has("description") ? json.get("description").asText() : null)
                        .contentUrls(json.has("content_urls") ? json.get("content_urls").asText() : null)
                        .price(json.has("price") ? 
                            new BigDecimal(json.get("price").asText()) : null)
                        .amount(json.has("amount") ? json.get("amount").asInt() : null)
                        .status(json.has("status") ? json.get("status").asText() : null)
                        .deleted(json.has("deleted") ? json.get("deleted").asBoolean() : null)
                        .build();
                } catch (Exception e) {
                    log.error("Error parsing product JSON: {}", e.getMessage());
                    return null;
                }
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
    
    @Override
    public List<ProductDocument> getSimilarProducts(UUID productId, int size) {
        Optional<ProductDocument> productOpt = productElasticsearchRepository.findById(productId);
        if (productOpt.isEmpty()) {
            log.warn("Product not found with id: {}", productId);
            return Collections.emptyList();
        }

        ProductDocument product = productOpt.get();
        String categoryId = product.getCategoryId() != null ? (product.getCategoryId().toString()) : null;
        String shopId = product.getShopId() != null ? product.getShopId().toString() : null;

        log.info("Finding similar products for product: {}, categoryIds: {}, shopIds: {}, size: {}",
                productId, categoryId, shopId, size);

        Set<ProductDocument> similarProducts = new HashSet<>();

        // Add products based on category and shop
        if (categoryId != null || shopId != null) {
            try {
                List<ProductDocument> categoryShopProducts = similarProductsBasedOnCategoryAndShop(categoryId, shopId, size);
                similarProducts.addAll(categoryShopProducts);
            } catch (Exception e) {
                log.error("Error getting similar products based on category and shop", e);
            }
        }


        // Get tokens from product name analysis
        List<String> tokens = analyzeText(product.getName());
        log.info("Analyzed tokens for product name: {}", tokens);

        // Add products based on name tokens
        for (String token : tokens) {
            List<ProductDocument> tokenSimilarProducts = productElasticsearchRepository
                    .suggestByNameOrDescription(token);
            similarProducts.addAll(tokenSimilarProducts);
        }

        // Remove the original product and limit the size
        similarProducts.remove(product);
        List<ProductDocument> result = similarProducts.stream()
                .limit(size)
                .collect(Collectors.toList());

        log.info("Found {} similar products", result.size());
        return result;
    }

    @Override
    public List<ProductDocument> getSimilarProductsFromRecentSearches(UUID userId, int size) {
        log.info("Getting similar products from recent searches for user: {}", userId);

        List<String> searchQueries;
        if (userId != null) {
            // Lấy 5 từ khóa tìm kiếm gần nhất của user cụ thể
            Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Order.desc("timestamp")));
            List<UserBehaviorDocument> recentSearches = userBehaviorElasticsearchRepository.findRecentSearches(userId,
                    pageable);
            searchQueries = recentSearches.stream()
                    .map(UserBehaviorDocument::getSearchQuery)
                    .collect(Collectors.toList());
            if (searchQueries.isEmpty()) {
                searchQueries = getTopSearchQueriesWithoutUserId();
            }
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
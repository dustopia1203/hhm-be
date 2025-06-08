package com.hhm.api.service.impl;

import com.hhm.api.model.dto.PageDTO;
import com.hhm.api.model.dto.mapper.AutoMapper;
import com.hhm.api.model.dto.request.IdsRequest;
import com.hhm.api.model.dto.request.ProductCreateOrUpdateRequest;
import com.hhm.api.model.dto.request.ProductSearchRequest;
import com.hhm.api.model.dto.response.CategoryResponse;
import com.hhm.api.model.dto.response.ProductResponse;
import com.hhm.api.model.elasticsearch.ProductDocument;
import com.hhm.api.model.elasticsearch.UserBehaviorDocument;
import com.hhm.api.model.entity.Category;
import com.hhm.api.model.entity.Product;
import com.hhm.api.model.entity.Shop;
import com.hhm.api.model.entity.projection.ReviewStat;
import com.hhm.api.repository.CategoryRepository;
import com.hhm.api.repository.OrderItemRepository;
import com.hhm.api.repository.ProductRepository;
import com.hhm.api.repository.ReviewRepository;
import com.hhm.api.repository.ShopRepository;
import com.hhm.api.repository.elasticsearch.ProductElasticsearchRepository;
import com.hhm.api.service.ProductRecommendationService;
import com.hhm.api.service.ProductService;
import com.hhm.api.support.enums.ActiveStatus;
import com.hhm.api.support.enums.error.AuthorizationError;
import com.hhm.api.support.enums.error.BadRequestError;
import com.hhm.api.support.enums.error.InternalServerError;
import com.hhm.api.support.enums.error.NotFoundError;
import com.hhm.api.support.exception.ResponseException;
import com.hhm.api.support.util.IdUtils;
import com.hhm.api.support.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ShopRepository shopRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final AutoMapper autoMapper;
    private final ReviewRepository reviewRepository;
    private final ProductElasticsearchRepository productElasticsearchRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRecommendationService productRecommendationService;

    @Override
    public PageDTO<Product> search(ProductSearchRequest request) {
        // Lưu lịch sử tìm kiếm nếu có userid
        if (request.getKeyword() != null && !request.getKeyword().trim().isEmpty()) {
            try {
                UUID userId = SecurityUtils.getCurrentUserId();
                UserBehaviorDocument searchBehavior = UserBehaviorDocument.builder()
                    .userId(userId)
                    .behaviorType("SEARCH")
                    .searchQuery(request.getKeyword().trim())
                    .timestamp(Instant.now())
                    .build();
                productRecommendationService.trackUserBehavior(searchBehavior);
                System.out.println("lưu lại lịch sử tìm kiếm");
            } catch (ResponseException e) {
                // Nếu không có token hoặc token không hợp lệ thì bỏ qua, không lưu history
                System.out.println("No valid token found, skipping search history tracking");
            }
        }
        Long count = productRepository.count(request);

        if (Objects.equals(count, 0L)) {
            return PageDTO.empty(request.getPageIndex(), request.getPageSize());
        }

        List<Product> products = productRepository.search(request);

        return PageDTO.of(products, request.getPageIndex(), request.getPageSize(), count);
    }

    @Override
    public ProductResponse getById(UUID id) {
        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isEmpty()) {
            throw new ResponseException(NotFoundError.PRODUCT_NOT_FOUND);
        }

        Product product = productOptional.get();

        ProductResponse response = autoMapper.toResponse(product);

        response.setSoldCount(Math.toIntExact(orderItemRepository.countByProduct(product.getId())));

        List<String> images = List.of(product.getContentUrls().split(";"));

        response.setImages(images);

        ReviewStat reviewStat = reviewRepository.findStatByProduct(id);

        response.setReviewCount(Objects.nonNull(reviewStat.getReviewCount()) ? reviewStat.getReviewCount() : 0L);
        response.setRating(Objects.nonNull(reviewStat.getAvgRating()) ? reviewStat.getAvgRating() : 0);

        List<Category> categories = categoryRepository.findTreeById(product.getCategoryId());

        Optional<Category> rootCategoryOptional = categories.stream()
                .filter(category -> Objects.isNull(category.getParentId()))
                .findFirst();

        if (rootCategoryOptional.isEmpty()) {
            throw new ResponseException(InternalServerError.INTERNAL_SERVER_ERROR);
        }

        Category rootCategory = rootCategoryOptional.get();

        CategoryResponse categoryResponse = buildCategoryTree(rootCategory, categories);

        response.setCategory(categoryResponse);

        return response;
    }

    @Override
    public Product createMyShop(ProductCreateOrUpdateRequest request) {
        UUID userId = SecurityUtils.getCurrentUserId();

        validateMyShopAndCategory(userId, request);

        Product product = Product.builder()
                .id(IdUtils.nextId())
                .shopId(request.getShopId())
                .categoryId(request.getCategoryId())
                .name(request.getName())
                .description(request.getDescription())
                .contentUrls(request.getContentUrls())
                .price(request.getPrice())
                .amount(request.getAmount())
                .status(ActiveStatus.ACTIVE)
                .deleted(Boolean.FALSE)
                .build();

        productRepository.save(product);
        syncProductToElasticsearch(product);

        return product;
    }

    @Override
    public Product updateMyShop(UUID id, ProductCreateOrUpdateRequest request) {
        UUID userId = SecurityUtils.getCurrentUserId();

        Optional<Product> productOptional = productRepository.findById(id);

        if (productOptional.isEmpty()) {
            throw new ResponseException(NotFoundError.PRODUCT_NOT_FOUND);
        }

        validateMyShopAndCategory(userId, request);

        Product product = productOptional.get();

        product.setShopId(request.getShopId());
        product.setCategoryId(request.getCategoryId());
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setContentUrls(request.getContentUrls());
        product.setPrice(request.getPrice());
        product.setAmount(request.getAmount());

        productRepository.save(product);
        syncProductToElasticsearch(product);

        return product;
    }

    private void validateMyShopAndCategory(UUID userId, ProductCreateOrUpdateRequest request) {
        Optional<Shop> shopOptional = shopRepository.findActiveById(request.getShopId());

        if (shopOptional.isEmpty()) {
            throw new ResponseException(NotFoundError.SHOP_NOT_FOUND);
        }

        Shop shop = shopOptional.get();

        if (!Objects.equals(shop.getUserId(), userId)) {
            throw new ResponseException(AuthorizationError.ACCESS_DENIED);
        }

        Optional<Category> categoryOptional = categoryRepository.findActiveById(request.getCategoryId());

        if (categoryOptional.isEmpty()) {
            throw new ResponseException(NotFoundError.CATEGORY_NOT_FOUND);
        }
    }

    @Override
    public void activeMyShop(IdsRequest request) {
        List<Product> products = productRepository.findByIds(request.getIds());

        request.getIds().forEach(id -> {
            Optional<Product> productOptional = products.stream()
                    .filter(product -> Objects.equals(product.getId(), id))
                    .findFirst();

            if (productOptional.isEmpty()) {
                throw new ResponseException(NotFoundError.PRODUCT_NOT_FOUND);
            }

            Product product = productOptional.get();

            if (Objects.equals(product.getStatus(), ActiveStatus.ACTIVE)) {
                throw new ResponseException(BadRequestError.PRODUCT_WAS_ACTIVATED);
            }

            product.setStatus(ActiveStatus.ACTIVE);
        });

        productRepository.saveAll(products);
        products.forEach(this::syncProductToElasticsearch);
    }

    @Override
    public void inactiveMyShop(IdsRequest request) {
        List<Product> products = productRepository.findByIds(request.getIds());

        request.getIds().forEach(id -> {
            Optional<Product> productOptional = products.stream()
                    .filter(product -> Objects.equals(product.getId(), id))
                    .findFirst();

            if (productOptional.isEmpty()) {
                throw new ResponseException(NotFoundError.PRODUCT_NOT_FOUND);
            }

            Product product = productOptional.get();

            if (Objects.equals(product.getStatus(), ActiveStatus.INACTIVE)) {
                throw new ResponseException(BadRequestError.PRODUCT_WAS_INACTIVATED);
            }

            product.setStatus(ActiveStatus.INACTIVE);
        });

        productRepository.saveAll(products);
        products.forEach(this::syncProductToElasticsearch);
    }

    @Override
    public void deleteMyShop(IdsRequest request) {
        List<Product> products = productRepository.findByIds(request.getIds());

        request.getIds().forEach(id -> {
            Optional<Product> productOptional = products.stream()
                    .filter(product -> Objects.equals(product.getId(), id))
                    .findFirst();

            if (productOptional.isEmpty()) {
                throw new ResponseException(NotFoundError.PRODUCT_NOT_FOUND);
            }

            Product product = productOptional.get();

            product.setDeleted(Boolean.TRUE);
        });

        productRepository.saveAll(products);
        products.forEach(this::syncProductToElasticsearch);
    }

    private CategoryResponse buildCategoryTree(Category parent, List<Category> categories) {
        List<CategoryResponse> subCategoryResponses = categories.stream()
                .filter(category -> Objects.equals(category.getParentId(), parent.getId()))
                .map(category -> buildCategoryTree(category, categories))
                .toList();

        if (CollectionUtils.isEmpty(subCategoryResponses)) subCategoryResponses = null;

        return CategoryResponse.builder()
                .id(parent.getId())
                .name(parent.getName())
                .subCategories(subCategoryResponses)
                .build();
    }

    private void syncProductToElasticsearch(Product product) {
        ProductDocument doc = ProductDocument.builder()
                .id(product.getId())
                .shopId(product.getShopId())
                .categoryId(product.getCategoryId())
                .name(product.getName())
                .description(product.getDescription())
                .contentUrls(product.getContentUrls())
                .price(product.getPrice())
                .amount(product.getAmount())
                .status(product.getStatus().name())
                .deleted(product.getDeleted())
                .build();
        productElasticsearchRepository.save(doc);
    }
}

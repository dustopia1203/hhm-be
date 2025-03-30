package com.hhm.api.service.impl;

import com.hhm.api.model.dto.PageDTO;
import com.hhm.api.model.dto.request.CategoryCreateOrUpdateRequest;
import com.hhm.api.model.dto.request.CategorySearchRequest;
import com.hhm.api.model.dto.request.IdsRequest;
import com.hhm.api.model.dto.response.CategoryResponse;
import com.hhm.api.model.entity.Category;
import com.hhm.api.repository.CategoryRepository;
import com.hhm.api.service.CategoryService;
import com.hhm.api.support.enums.ActiveStatus;
import com.hhm.api.support.enums.error.BadRequestError;
import com.hhm.api.support.enums.error.NotFoundError;
import com.hhm.api.support.exception.ResponseException;
import com.hhm.api.support.util.IdUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public PageDTO<Category> search(CategorySearchRequest request) {
        Long count = categoryRepository.count(request);

        if (Objects.equals(count, 0L)) return PageDTO.empty(request.getPageIndex(), request.getPageSize());

        List<Category> categories = categoryRepository.search(request);

        return PageDTO.of(categories, request.getPageIndex(), request.getPageSize(), count);
    }

    @Override
    public CategoryResponse getById(UUID id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);

        if (categoryOptional.isEmpty()) {
            throw new ResponseException(NotFoundError.CATEGORY_NOT_FOUND);
        }

        Category category = categoryOptional.get();

        List<Category> categories = categoryRepository.findTreeByParent(id);

        return buildCategoryTree(category, categories);
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

    @Override
    public Category create(CategoryCreateOrUpdateRequest request) {
        if (Objects.nonNull(request.getParentId())) {
            Optional<Category> parentOptional = categoryRepository.findById(request.getParentId());

            if (parentOptional.isEmpty()) {
                throw new ResponseException(NotFoundError.CATEGORY_NOT_FOUND);
            }
        }

        Category category = Category.builder()
                .id(IdUtils.nextId())
                .parentId(request.getParentId())
                .name(request.getName())
                .status(ActiveStatus.ACTIVE)
                .deleted(Boolean.FALSE)
                .build();

        categoryRepository.save(category);

        return category;
    }

    @Override
    public Category update(UUID id, CategoryCreateOrUpdateRequest request) {
        List<UUID> categoryIds = new ArrayList<>();

        categoryIds.add(id);

        if (Objects.nonNull(request.getParentId())) {
            if (Objects.equals(id, request.getParentId())) throw new ResponseException(BadRequestError.INVALID_PARENT_CATEGORY);

            categoryIds.add(request.getParentId());
        }

        List<Category> categories = categoryRepository.findByIds(categoryIds);

        if (CollectionUtils.isEmpty(categories) || !Objects.equals(categoryIds.size(), categories.size())) {
            throw new ResponseException(NotFoundError.CATEGORY_NOT_FOUND);
        }

        Category category = categories.stream()
                .filter(item -> Objects.equals(item.getId(), id))
                .findFirst()
                .orElseThrow(() -> new ResponseException(NotFoundError.CATEGORY_NOT_FOUND));

        category.setParentId(request.getParentId());
        category.setName(request.getName());

        categoryRepository.save(category);

        return category;
    }

    @Override
    public void active(IdsRequest request) {
        List<Category> categories = categoryRepository.findByIds(request.getIds());

        request.getIds().forEach(id -> {
            Optional<Category> categoryOptional = categories.stream()
                    .filter(category -> Objects.equals(category.getId(), id))
                    .findFirst();

            if (categoryOptional.isEmpty()) {
                throw new ResponseException(NotFoundError.CATEGORY_NOT_FOUND);
            }

            Category category = categoryOptional.get();

            if (Objects.equals(category.getStatus(), ActiveStatus.ACTIVE)) {
                throw new ResponseException(BadRequestError.CATEGORY_WAS_ACTIVATED);
            }

            category.setStatus(ActiveStatus.ACTIVE);
        });

        categoryRepository.saveAll(categories);
    }

    @Override
    public void inactive(IdsRequest request) {
        List<Category> categories = categoryRepository.findByIds(request.getIds());

        request.getIds().forEach(id -> {
            Optional<Category> categoryOptional = categories.stream()
                    .filter(category -> Objects.equals(category.getId(), id))
                    .findFirst();

            if (categoryOptional.isEmpty()) {
                throw new ResponseException(NotFoundError.CATEGORY_NOT_FOUND);
            }

            Category category = categoryOptional.get();

            if (Objects.equals(category.getStatus(), ActiveStatus.INACTIVE)) {
                throw new ResponseException(BadRequestError.CATEGORY_WAS_INACTIVATED);
            }

            category.setStatus(ActiveStatus.INACTIVE);
        });

        categoryRepository.saveAll(categories);
    }

    @Override
    public void delete(IdsRequest request) {
        List<Category> categories = categoryRepository.findByIds(request.getIds());

        request.getIds().forEach(id -> {
            Optional<Category> categoryOptional = categories.stream()
                    .filter(category -> Objects.equals(category.getId(), id))
                    .findFirst();

            if (categoryOptional.isEmpty()) {
                throw new ResponseException(NotFoundError.CATEGORY_NOT_FOUND);
            }

            Category category = categoryOptional.get();

            category.setDeleted(Boolean.TRUE);
        });

        categoryRepository.saveAll(categories);
    }
}

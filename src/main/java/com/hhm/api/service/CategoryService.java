package com.hhm.api.service;

import com.hhm.api.model.dto.PageDTO;
import com.hhm.api.model.dto.request.CategoryCreateOrUpdateRequest;
import com.hhm.api.model.dto.request.CategorySearchRequest;
import com.hhm.api.model.dto.request.IdsRequest;
import com.hhm.api.model.dto.response.CategoryResponse;
import com.hhm.api.model.entity.Category;

import java.util.UUID;

public interface CategoryService {
    PageDTO<Category> search(CategorySearchRequest request);

    CategoryResponse getById(UUID id);

    Category create(CategoryCreateOrUpdateRequest request);

    Category update(UUID id, CategoryCreateOrUpdateRequest request);

    void active(IdsRequest request);

    void inactive(IdsRequest request);

    void delete(IdsRequest request);
}

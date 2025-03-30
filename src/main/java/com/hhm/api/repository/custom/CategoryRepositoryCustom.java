package com.hhm.api.repository.custom;

import com.hhm.api.model.dto.request.CategorySearchRequest;
import com.hhm.api.model.entity.Category;

import java.util.List;

public interface CategoryRepositoryCustom {
    Long count(CategorySearchRequest request);

    List<Category> search(CategorySearchRequest request);
}

package com.hhm.api.presentation.web.rest.impl;

import com.hhm.api.model.dto.request.CategoryCreateOrUpdateRequest;
import com.hhm.api.model.dto.request.CategorySearchRequest;
import com.hhm.api.model.dto.request.IdsRequest;
import com.hhm.api.model.dto.response.CategoryResponse;
import com.hhm.api.model.dto.response.PagingResponse;
import com.hhm.api.model.dto.response.Response;
import com.hhm.api.model.entity.Category;
import com.hhm.api.presentation.web.rest.CategoryController;
import com.hhm.api.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CategoryControllerImpl implements CategoryController {
    private final CategoryService categoryService;

    @Override
    public PagingResponse<Category> search(CategorySearchRequest request) {
        return PagingResponse.of(categoryService.search(request));
    }

    @Override
    public Response<CategoryResponse> getById(UUID id) {
        return Response.of(categoryService.getById(id));
    }

    @Override
    public Response<Category> create(CategoryCreateOrUpdateRequest request) {
        return Response.of(categoryService.create(request));
    }

    @Override
    public Response<Category> update(UUID id, CategoryCreateOrUpdateRequest request) {
        return Response.of(categoryService.update(id, request));
    }

    @Override
    public Response<Boolean> active(IdsRequest request) {
        categoryService.active(request);
        return Response.ok();
    }

    @Override
    public Response<Boolean> inactive(IdsRequest request) {
        categoryService.inactive(request);
        return null;
    }

    @Override
    public Response<Boolean> delete(IdsRequest request) {
        categoryService.delete(request);
        return null;
    }
}

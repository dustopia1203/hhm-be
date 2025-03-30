package com.hhm.api.presentation.web.rest;

import com.hhm.api.config.application.validator.ValidatePaging;
import com.hhm.api.model.dto.request.CategoryCreateOrUpdateRequest;
import com.hhm.api.model.dto.request.CategorySearchRequest;
import com.hhm.api.model.dto.request.IdsRequest;
import com.hhm.api.model.dto.response.CategoryResponse;
import com.hhm.api.model.dto.response.PagingResponse;
import com.hhm.api.model.dto.response.Response;
import com.hhm.api.model.entity.Category;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Tag(name = "Category Resources")
@RequestMapping("/api/categories")
@Validated
public interface CategoryController {
    @Operation(summary = "Search categories")
    @GetMapping("/q")
    PagingResponse<Category> search(@ValidatePaging(sortModel = Category.class) CategorySearchRequest request);

    @Operation(summary = "Get category tree by id")
    @GetMapping("/{id}")
    Response<CategoryResponse> getById(@PathVariable UUID id);

    @Operation(summary = "Create new category")
    @PostMapping("")
    @PreAuthorize("hasPermission(null, 'CATEGORY:CREATE')")
    Response<Category> create(@Valid @RequestBody CategoryCreateOrUpdateRequest request);

    @Operation(summary = "Update category")
    @PutMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'CATEGORY:UPDATE')")
    Response<Category> update(
            @PathVariable UUID id,
            @Valid @RequestBody CategoryCreateOrUpdateRequest request
    );

    @Operation(summary = "Active categories")
    @PutMapping("/active")
    @PreAuthorize("hasPermission(null, 'CATEGORY:UPDATE')")
    Response<Boolean> active(@RequestBody IdsRequest request);

    @Operation(summary = "Inactive categories")
    @PutMapping("/inactive")
    @PreAuthorize("hasPermission(null, 'CATEGORY:UPDATE')")
    Response<Boolean> inactive(@RequestBody IdsRequest request);

    @Operation(summary = "Delete categories")
    @DeleteMapping("")
    @PreAuthorize("hasPermission(null, 'CATEGORY:DELETE')")
    Response<Boolean> delete(@RequestBody IdsRequest request);
}

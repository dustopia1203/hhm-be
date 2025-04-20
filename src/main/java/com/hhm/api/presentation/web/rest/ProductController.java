package com.hhm.api.presentation.web.rest;

import com.hhm.api.config.application.validator.ValidatePaging;
import com.hhm.api.model.dto.request.IdsRequest;
import com.hhm.api.model.dto.request.ProductCreateOrUpdateRequest;
import com.hhm.api.model.dto.request.ProductSearchRequest;
import com.hhm.api.model.dto.response.PagingResponse;
import com.hhm.api.model.dto.response.Response;
import com.hhm.api.model.entity.Product;
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

@Tag(name = "Product Resources")
@RequestMapping("/api/products")
@Validated
public interface ProductController {
    @Operation(summary = "Search products")
    @GetMapping("/q")
    PagingResponse<Product> search(@ValidatePaging(sortModel = Product.class) ProductSearchRequest request);

    @Operation(summary = "Get product by id")
    @GetMapping("/{id}")
    Response<Product> getById(@PathVariable UUID id);

    @Operation(summary = "Create new product")
    @PostMapping("")
    @PreAuthorize("hasPermission(null, 'PRODUCT:SELF_CREATE')")
    Response<Product> create(@Valid @RequestBody ProductCreateOrUpdateRequest request);

    @Operation(summary = "Update product")
    @PutMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'PRODUCT:SELF_UPDATE')")
    Response<Product> update(
            @PathVariable UUID id,
            @Valid @RequestBody ProductCreateOrUpdateRequest request
    );

    @Operation(summary = "Active products")
    @PutMapping("/active")
    @PreAuthorize("hasPermission(null, 'PRODUCT:UPDATE')")
    Response<Boolean> active(@RequestBody IdsRequest request);

    @Operation(summary = "Inactive products")
    @PutMapping("/inactive")
    @PreAuthorize("hasPermission(null, 'PRODUCT:UPDATE')")
    Response<Boolean> inactive(@RequestBody IdsRequest request);

    @Operation(summary = "Delete products")
    @DeleteMapping("")
    @PreAuthorize("hasPermission(null, 'PRODUCT:DELETE')")
    Response<Boolean> delete(@RequestBody IdsRequest request);
}

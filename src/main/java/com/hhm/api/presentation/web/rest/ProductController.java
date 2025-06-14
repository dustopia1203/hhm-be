package com.hhm.api.presentation.web.rest;

import com.hhm.api.config.application.validator.ValidatePaging;
import com.hhm.api.model.dto.request.IdsRequest;
import com.hhm.api.model.dto.request.ProductCreateOrUpdateRequest;
import com.hhm.api.model.dto.request.ProductSearchRequest;
import com.hhm.api.model.dto.response.PagingResponse;
import com.hhm.api.model.dto.response.ProductResponse;
import com.hhm.api.model.dto.response.Response;
import com.hhm.api.model.entity.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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
    PagingResponse<ProductResponse> search(@ValidatePaging(sortModel = Product.class) ProductSearchRequest request);

    @Operation(summary = "Get product by id")
    @GetMapping("/{id}")
    Response<ProductResponse> getById(@PathVariable UUID id);

    @Operation(summary = "Create my shop product")
    @PostMapping("/my/shop")
    Response<Product> createMyShop(@Valid @RequestBody ProductCreateOrUpdateRequest request);

    @Operation(summary = "Update my shop product")
    @PutMapping("/my/shop/{id}")
    Response<Product> updateMyShop(
            @PathVariable UUID id,
            @Valid @RequestBody ProductCreateOrUpdateRequest request
    );

    @Operation(summary = "Active my shop products")
    @PutMapping("/my/shop/active")
    Response<Boolean> activeMyShop(@RequestBody IdsRequest request);

    @Operation(summary = "Inactive my shop products")
    @PutMapping("/my/shop/inactive")
    Response<Boolean> inactiveMyShop(@RequestBody IdsRequest request);

    @Operation(summary = "Delete my shop products")
    @DeleteMapping("/my/shop")
    Response<Boolean> deleteMyShop(@RequestBody IdsRequest request);
}

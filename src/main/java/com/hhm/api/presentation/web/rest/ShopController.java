package com.hhm.api.presentation.web.rest;

import com.hhm.api.config.application.validator.ValidatePaging;
import com.hhm.api.model.dto.request.IdsRequest;
import com.hhm.api.model.dto.request.ShopCreateOrUpdateRequest;
import com.hhm.api.model.dto.request.ShopSearchRequest;
import com.hhm.api.model.dto.response.PagingResponse;
import com.hhm.api.model.dto.response.Response;
import com.hhm.api.model.dto.response.ShopDetailResponse;
import com.hhm.api.model.entity.Shop;
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

@Tag(name = "Shop Resources")
@RequestMapping("/api/shops")
@Validated
public interface ShopController {
    @Operation(summary = "Search shops")
    @GetMapping("/q")
    PagingResponse<Shop> search(@ValidatePaging(sortModel = Shop.class) ShopSearchRequest request);

    @Operation(summary = "Get shop by id")
    @GetMapping("/{id}")
    Response<ShopDetailResponse> getById(@PathVariable UUID id);

    @Operation(summary = "Get my shop")
    @GetMapping("/my")
    Response<ShopDetailResponse> getMy();

    @Operation(summary = "Create my shop")
    @PostMapping("/my")
    @PreAuthorize("hasPermission(null, 'SHOP:SELF_CREATE')")
    Response<Shop> createMy(@Valid @RequestBody ShopCreateOrUpdateRequest request);

    @Operation(summary = "Update my shop")
    @PutMapping("/my")
    @PreAuthorize("hasPermission(null, 'SHOP:SELF_UPDATE')")
    Response<Shop> updateMy(@Valid @RequestBody ShopCreateOrUpdateRequest request);

    @Operation(summary = "Active shops")
    @PutMapping("/active")
    @PreAuthorize("hasPermission(null, 'SHOP:UPDATE')")
    Response<Boolean> active(@RequestBody IdsRequest request);

    @Operation(summary = "Inactive shops")
    @PutMapping("/inactive")
    @PreAuthorize("hasPermission(null, 'SHOP:UPDATE')")
    Response<Boolean> inactive(@RequestBody IdsRequest request);

    @Operation(summary = "Delete shops")
    @DeleteMapping("")
    @PreAuthorize("hasPermission(null, 'SHOP:DELETE')")
    Response<Boolean> delete(@RequestBody IdsRequest request);
}

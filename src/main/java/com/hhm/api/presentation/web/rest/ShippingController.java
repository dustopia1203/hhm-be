package com.hhm.api.presentation.web.rest;

import com.hhm.api.config.application.validator.ValidatePaging;
import com.hhm.api.model.dto.request.ShippingCreateOrUpdateRequest;
import com.hhm.api.model.dto.request.IdsRequest;
import com.hhm.api.model.dto.request.ShippingSearchRequest;
import com.hhm.api.model.dto.response.PagingResponse;
import com.hhm.api.model.dto.response.Response;
import com.hhm.api.model.entity.Shipping;
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

@Tag(name = "Shipping Service Resources")
@RequestMapping("/api/shipping")
@Validated
public interface ShippingController {
    @Operation(summary = "Search shipping services")
    @GetMapping("/q")
    PagingResponse<Shipping> search(@ValidatePaging(sortModel = Shipping.class) ShippingSearchRequest request);

    @Operation(summary = "Create shipping service")
    @PostMapping("")
    @PreAuthorize("hasPermission(null, 'SHIPPING:CREATE')")
    Response<Shipping> create(@Valid @RequestBody ShippingCreateOrUpdateRequest request);

    @Operation(summary = "Update shipping service")
    @PutMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'SHIPPING:UPDATE')")
    Response<Shipping> update(@PathVariable UUID id,
                              @Valid @RequestBody ShippingCreateOrUpdateRequest request);

    @Operation(summary = "Active shipping services")
    @PutMapping("/active")
    @PreAuthorize("hasPermission(null, 'SHIPPING:UPDATE')")
    Response<Boolean> active(@RequestBody IdsRequest request);

    @Operation(summary = "Inactive shipping services")
    @PutMapping("/inactive")
    @PreAuthorize("hasPermission(null, 'SHIPPING:UPDATE')")
    Response<Boolean> inactive(@RequestBody IdsRequest request);

    @Operation(summary = "Delete shipping services")
    @DeleteMapping("")
    @PreAuthorize("hasPermission(null, 'SHIPPING:DELETE')")
    Response<Boolean> delete(@RequestBody IdsRequest request);
}

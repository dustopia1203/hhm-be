package com.hhm.api.presentation.web.rest;

import com.hhm.api.config.application.validator.ValidatePaging;
import com.hhm.api.model.dto.request.IdsRequest;
import com.hhm.api.model.dto.request.UserSearchRequest;
import com.hhm.api.model.dto.response.PagingResponse;
import com.hhm.api.model.dto.response.Response;
import com.hhm.api.model.dto.response.UserDetailResponse;
import com.hhm.api.model.entity.Role;
import com.hhm.api.model.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Tag(name = "User Resources")
@RequestMapping("/api/users")
@Validated
public interface UserController {
    @Operation(summary = "Search users")
    @GetMapping("/q")
    @PreAuthorize("hasPermission(null, 'USER:READ')")
    PagingResponse<User> search(@ValidatePaging(sortModel = User.class) UserSearchRequest request);

    @Operation(summary = "Get user detail by id")
    @GetMapping("/{id}")
    @PreAuthorize("hasPermission(null, 'USER:READ')")
    Response<UserDetailResponse> getDetailById(@Valid @PathVariable UUID id);

    @Operation(summary = "Active users")
    @PutMapping("/active")
    @PreAuthorize("hasPermission(null, 'USER:UPDATE')")
    Response<Boolean> active(@RequestBody IdsRequest request);

    @Operation(summary = "Inactive users")
    @PutMapping("/inactive")
    @PreAuthorize("hasPermission(null, 'USER:UPDATE')")
    Response<Boolean> inactive(@RequestBody IdsRequest request);

    @Operation(summary = "Delete users")
    @DeleteMapping("")
    @PreAuthorize("hasPermission(null, 'USER:DELETE')")
    Response<Boolean> delete(@RequestBody IdsRequest request);

    @Operation(summary = "Get user roles")
    @GetMapping("/{id}/roles")
    @PreAuthorize("hasPermission(null, 'USER:READ')")
    Response<List<Role>> getUserRoles(@PathVariable UUID id);

    @Operation(summary = "Set user role")
    @PutMapping("/{id}/roles")
    @PreAuthorize("hasPermission(null, 'USER:UPDATE')")
    Response<Boolean> setUserRole(@PathVariable UUID id, @RequestBody IdsRequest request);
}

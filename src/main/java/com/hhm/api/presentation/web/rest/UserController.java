package com.hhm.api.presentation.web.rest;

import com.hhm.api.config.application.validator.ValidatePaging;
import com.hhm.api.model.dto.request.PagingRequest;
import com.hhm.api.model.dto.request.UserSearchRequest;
import com.hhm.api.model.dto.response.PagingResponse;
import com.hhm.api.model.dto.response.Response;
import com.hhm.api.model.dto.response.UserDetailResponse;
import com.hhm.api.model.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Tag(name = "UserResources")
@RequestMapping("/api/users")
@Validated
public interface UserController {
    @Operation(summary = "Get user detail by id")
    @GetMapping("/{id}")
    Response<UserDetailResponse> getUserDetailById(@PathVariable @Valid UUID id);
    @Operation(summary = "Search user by id")
    @GetMapping("/q")
    PagingResponse<User> search(@ValidatePaging(sortModel = User.class)UserSearchRequest request);
}

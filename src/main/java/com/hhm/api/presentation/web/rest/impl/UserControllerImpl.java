package com.hhm.api.presentation.web.rest.impl;

import com.hhm.api.model.dto.request.IdsRequest;
import com.hhm.api.model.dto.request.UserSearchRequest;
import com.hhm.api.model.dto.response.PagingResponse;
import com.hhm.api.model.dto.response.Response;
import com.hhm.api.model.dto.response.UserDetailResponse;
import com.hhm.api.model.entity.Role;
import com.hhm.api.model.entity.User;
import com.hhm.api.presentation.web.rest.UserController;
import com.hhm.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {
    private final UserService userService;

    @Override
    public Response<UserDetailResponse> getDetailById(UUID id) {
        return Response.of(userService.getDetailById(id));
    }

    @Override
    public PagingResponse<User> search(UserSearchRequest request) {
        return PagingResponse.of(userService.search(request));
    }

    @Override
    public Response<Boolean> active(IdsRequest request) {
        userService.active(request);
        return Response.ok();
    }

    @Override
    public Response<Boolean> inactive(IdsRequest request) {
        userService.inactive(request);
        return Response.ok();
    }

    @Override
    public Response<Boolean> delete(IdsRequest request) {
        userService.delete(request);
        return Response.ok();
    }

    @Override
    public Response<List<Role>> getUserRoles(UUID id) {
        return Response.of(userService.getUserRoles(id));
    }

    @Override
    public Response<Boolean> setUserRole(UUID id, IdsRequest request) {
        userService.setUserRole(id, request);
        return Response.ok();
    }
}

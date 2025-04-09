package com.hhm.api.presentation.web.rest.impl;

import com.hhm.api.model.dto.request.UserSearchRequest;
import com.hhm.api.model.dto.response.PagingResponse;
import com.hhm.api.model.dto.response.Response;
import com.hhm.api.model.dto.response.UserDetailResponse;
import com.hhm.api.model.entity.User;
import com.hhm.api.presentation.web.rest.UserController;
import com.hhm.api.repository.UserRepository;
import com.hhm.api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
@RestController
@RequiredArgsConstructor
public class UserControllerImpl implements UserController {
    private final UserService userService;
    @Override
    public Response<UserDetailResponse> getUserDetailById(UUID id) {
        return Response.of(userService.getUserDetailById(id));
    }

    @Override
    public PagingResponse<User> search(UserSearchRequest request) {
        return PagingResponse.of(userService.search(request));
    }
}

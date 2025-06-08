package com.hhm.api.service;

import com.hhm.api.model.dto.PageDTO;
import com.hhm.api.model.dto.request.IdsRequest;
import com.hhm.api.model.dto.request.UserSearchRequest;
import com.hhm.api.model.dto.response.UserDetailResponse;
import com.hhm.api.model.entity.Role;
import com.hhm.api.model.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    void init();

    UserDetailResponse getDetailById(UUID id);

    PageDTO<User> search(UserSearchRequest request);

    void active(IdsRequest request);

    void inactive(IdsRequest request);

    void delete(IdsRequest request);

    List<Role> getUserRoles(UUID id);

    void setUserRole(UUID id, UUID roleId);
}

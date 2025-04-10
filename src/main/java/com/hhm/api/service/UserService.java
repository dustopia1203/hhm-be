package com.hhm.api.service;

import com.hhm.api.model.dto.PageDTO;
import com.hhm.api.model.dto.request.IdsRequest;
import com.hhm.api.model.dto.request.UserSearchRequest;
import com.hhm.api.model.dto.response.UserDetailResponse;
import com.hhm.api.model.entity.User;
import reactor.util.annotation.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    void init();
    User getUserById( UUID id);
    UserDetailResponse getUserDetailById(UUID id);
    PageDTO<User> search(UserSearchRequest request);
    void active(IdsRequest request);
    void inactive(IdsRequest request);
    void delete(IdsRequest request);
}

package com.hhm.api.repository.custom;

import com.hhm.api.model.dto.request.UserSearchRequest;
import com.hhm.api.model.entity.User;

import java.util.List;

public interface UserRepositoryCustom {
    Long count(UserSearchRequest request);

    List<User> search(UserSearchRequest request);
}

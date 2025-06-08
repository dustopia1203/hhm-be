package com.hhm.api.service;

import com.hhm.api.model.entity.Role;

import java.util.List;

public interface RoleService {
    void init();

    List<Role> getAvailableRoles();
}

package com.hhm.api.service.impl;

import com.hhm.api.model.dto.UserAuthority;
import com.hhm.api.model.entity.Role;
import com.hhm.api.model.entity.RolePrivilege;
import com.hhm.api.model.entity.User;
import com.hhm.api.model.entity.UserRole;
import com.hhm.api.repository.RolePrivilegeRepository;
import com.hhm.api.repository.RoleRepository;
import com.hhm.api.repository.UserRepository;
import com.hhm.api.repository.UserRoleRepository;
import com.hhm.api.service.AuthenticationService;
import com.hhm.api.support.enums.ActiveStatus;
import com.hhm.api.support.enums.error.NotFoundError;
import com.hhm.api.support.exception.ResponseException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolePrivilegeRepository rolePrivilegeRepository;
    private final RoleRepository roleRepository;

    @Override
    @Cacheable(
            cacheNames = "user-authority",
            key = "#id",
            condition = "#id != null",
            unless = "#result == null"
    )
    public UserAuthority getUserAuthority(UUID id) {
        User user = userRepository.findActiveById(id)
                .orElseThrow(() -> new ResponseException(NotFoundError.USER_NOT_FOUND));

        List<UserRole> userRoles = userRoleRepository.findByUserId(id);

        List<UUID> roleIds = userRoles.stream()
                .map(UserRole::getRoleId)
                .toList();

        List<Role> roles = roleRepository.findActiveByIds(roleIds);

        List<UUID> activeRoleIds = roles.stream()
                .filter(role -> Objects.equals(role.getStatus(), ActiveStatus.ACTIVE))
                .map(Role::getId)
                .toList();

        List<RolePrivilege> rolePrivileges = rolePrivilegeRepository.findAllByRoleId(activeRoleIds);

        List<String> privileges = new ArrayList<>();

        rolePrivileges.forEach(rolePrivilege -> {
            String privilege = rolePrivilege.getResourceCode().name() + ":" + rolePrivilege.getPermission().name();

            privileges.add(privilege);
        });

        return UserAuthority.builder()
                .userId(id)
                .username(user.getUsername())
                .grantedPrivileges(privileges.stream().distinct().toList())
                .build();
    }
}

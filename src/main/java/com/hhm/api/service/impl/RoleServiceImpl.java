package com.hhm.api.service.impl;

import com.hhm.api.model.entity.Role;
import com.hhm.api.model.entity.RolePrivilege;
import com.hhm.api.repository.RolePrivilegeRepository;
import com.hhm.api.repository.RoleRepository;
import com.hhm.api.service.RoleService;
import com.hhm.api.support.constants.Constants;
import com.hhm.api.support.enums.ActiveStatus;
import com.hhm.api.support.util.IdUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RolePrivilegeRepository rolePrivilegeRepository;

    @Override
    public void init() {
        List<String> defaultRoleCodes = Arrays.stream(Constants.DefaultRole.values())
                .map(Enum::name)
                .toList();

        List<Role> existedRoles = roleRepository.findAllByCodes(defaultRoleCodes);

        List<Role> roles = new ArrayList<>();
        List<RolePrivilege> rolePrivileges = new ArrayList<>();

        for (Constants.DefaultRole defaultRole : Constants.DefaultRole.values()) {
            Optional<Role> roleOptional = existedRoles.stream()
                    .filter(role -> Objects.equals(role.getCode(), defaultRole.name()))
                    .findFirst();

            if (roleOptional.isEmpty()) {
                Role role = Role.builder()
                        .id(IdUtils.nextId())
                        .code(defaultRole.name())
                        .name(defaultRole.name())
                        .status(ActiveStatus.ACTIVE)
                        .deleted(Boolean.FALSE)
                        .build();

                defaultRole.getPrivileges().forEach((resourceCode, permissions) -> {
                    permissions.forEach(permission -> {
                        RolePrivilege rolePrivilege = RolePrivilege.builder()
                                .id(IdUtils.nextId())
                                .roleId(role.getId())
                                .resourceCode(resourceCode)
                                .permission(permission)
                                .deleted(Boolean.FALSE)
                                .build();

                        rolePrivileges.add(rolePrivilege);
                    });
                });

                roles.add(role);
            }
        }

        roleRepository.saveAll(roles);
        rolePrivilegeRepository.saveAll(rolePrivileges);
    }

    @Override
    public List<Role> getAvailableRoles() {
        return roleRepository.findAllActive();
    }
}

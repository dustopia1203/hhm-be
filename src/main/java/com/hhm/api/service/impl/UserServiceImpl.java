package com.hhm.api.service.impl;

import com.hhm.api.model.entity.Role;
import com.hhm.api.model.entity.User;
import com.hhm.api.model.entity.UserRole;
import com.hhm.api.repository.RoleRepository;
import com.hhm.api.repository.UserRepository;
import com.hhm.api.repository.UserRoleRepository;
import com.hhm.api.service.UserService;
import com.hhm.api.support.constants.Constants;
import com.hhm.api.support.enums.AccountType;
import com.hhm.api.support.enums.ActiveStatus;
import com.hhm.api.support.util.IdUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    @Override
    public void init() {
        List<String> defaultUsernames = Arrays.stream(Constants.DefaultUser.values())
                .map(Constants.DefaultUser::getUsername)
                .toList();

        List<User> existedUsers = userRepository.findByUsernames(defaultUsernames);

        List<String> defaultUserRoleCodes = Arrays.stream(Constants.DefaultUser.values())
                .map(Constants.DefaultUser::getDefaultRole)
                .map(Constants.DefaultRole::name)
                .toList();

        List<Role> roles = roleRepository.findAllByCodes(defaultUserRoleCodes);

        List<User> users = new ArrayList<>();
        List<UserRole> userRoles = new ArrayList<>();

        for (Constants.DefaultUser defaultUser : Constants.DefaultUser.values()) {
            Optional<User> userOptional = existedUsers.stream()
                    .filter(user -> Objects.equals(user.getUsername(), defaultUser.getUsername()))
                    .findFirst();

            if (userOptional.isEmpty()) {
                User user = User.builder()
                        .id(IdUtils.nextId())
                        .username(defaultUser.getUsername())
                        .email(defaultUser.getEmail())
                        .password(passwordEncoder.encode(defaultUser.getPassword()))
                        .status(ActiveStatus.ACTIVE)
                        .accountType(AccountType.SYSTEM)
                        .deleted(Boolean.FALSE)
                        .build();

                Optional<Role> roleOptional = roles.stream()
                        .filter(role -> Objects.equals(role.getCode(), defaultUser.getDefaultRole().name()))
                        .findAny();

                roleOptional.ifPresent(role -> {
                    UserRole userRole = UserRole.builder()
                            .id(IdUtils.nextId())
                            .userId(user.getId())
                            .roleId(role.getId())
                            .deleted(Boolean.FALSE)
                            .build();

                    userRoles.add(userRole);
                });

                users.add(user);
            }
        }

        userRepository.saveAll(users);
        userRoleRepository.saveAll(userRoles);
    }
}

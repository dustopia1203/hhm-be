package com.hhm.api.service.impl;

import com.hhm.api.model.dto.PageDTO;
import com.hhm.api.model.dto.request.IdsRequest;
import com.hhm.api.model.dto.request.UserSearchRequest;
import com.hhm.api.model.dto.response.UserDetailResponse;
import com.hhm.api.model.entity.Role;
import com.hhm.api.model.entity.User;
import com.hhm.api.model.entity.UserInformation;
import com.hhm.api.model.entity.UserRole;
import com.hhm.api.repository.RoleRepository;
import com.hhm.api.repository.UserInformationRepository;
import com.hhm.api.repository.UserRepository;
import com.hhm.api.repository.UserRoleRepository;
import com.hhm.api.service.UserService;
import com.hhm.api.support.constants.Constants;
import com.hhm.api.support.enums.AccountType;
import com.hhm.api.support.enums.ActiveStatus;
import com.hhm.api.support.enums.error.BadRequestError;
import com.hhm.api.support.enums.error.NotFoundError;
import com.hhm.api.support.exception.ResponseException;
import com.hhm.api.support.util.IdUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final UserInformationRepository userInformationRepository;

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

    @Override
    public UserDetailResponse getDetailById(UUID id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isEmpty()) {
            throw new ResponseException(NotFoundError.USER_NOT_FOUND);
        }

        User user = userOptional.get();

        UserInformation userInformation = userInformationRepository.findById(user.getId()).orElse(null);

        List<UserRole> userRoles = userRoleRepository.findByUser(user.getId());

        return UserDetailResponse.builder()
                .username(user.getUsername())
                .firstName(Objects.nonNull(userInformation) ? userInformation.getFirstName() : null)
                .lastName(Objects.nonNull(userInformation) ? userInformation.getLastName() : null)
                .middleName(Objects.nonNull(userInformation) ? userInformation.getMiddleName() : null)
                .phone(Objects.nonNull(userInformation) ? userInformation.getPhone() : null)
                .dateOfBirth(Objects.nonNull(userInformation) ? userInformation.getDateOfBirth() : null)
                .avatarUrl(Objects.nonNull(userInformation) ? userInformation.getAvatarUrl() : null)
                .gender(Objects.nonNull(userInformation) ? userInformation.getGender() : null)
                .address(Objects.nonNull(userInformation) ? userInformation.getAddress() : null)
                .email(user.getEmail())
                .status(user.getStatus())
                .accountType(user.getAccountType())
                .userRoles(userRoles)
                .build();
    }

    @Override
    public PageDTO<User> search(UserSearchRequest request) {
        Long count = userRepository.count(request);

        if (Objects.equals(count, 0L)) {
            return PageDTO.empty(request.getPageIndex(), request.getPageSize());
        }

        List<User> users = userRepository.search(request);

        return PageDTO.of(users, request.getPageIndex(), request.getPageSize(), count);
    }

    @Override
    public void active(IdsRequest request) {
        List<User> users = userRepository.findByIds(request.getIds());

        request.getIds().forEach(id -> {
            Optional<User> userOptional = users.stream()
                    .filter(user -> Objects.equals(user.getId(), id))
                    .findFirst();

            if (userOptional.isEmpty()) {
                throw new ResponseException(NotFoundError.USER_NOT_FOUND);
            }

            User user = userOptional.get();

            if (Objects.equals(user.getStatus(), ActiveStatus.ACTIVE)) {
                throw new ResponseException(BadRequestError.USER_WAS_ACTIVATED);
            }

            user.setStatus(ActiveStatus.ACTIVE);
        });

        userRepository.saveAll(users);
    }

    @Override
    public void inactive(IdsRequest request) {
        List<User> users = userRepository.findByIds(request.getIds());

        request.getIds().forEach(id -> {
            Optional<User> userOptional = users.stream()
                    .filter(user -> Objects.equals(user.getId(), id))
                    .findFirst();

            if (userOptional.isEmpty()) {
                throw new ResponseException(NotFoundError.USER_NOT_FOUND);
            }

            User user = userOptional.get();

            if (Objects.equals(user.getStatus(), ActiveStatus.INACTIVE)) {
                throw new ResponseException(BadRequestError.USER_WAS_INACTIVATED);
            }

            user.setStatus(ActiveStatus.INACTIVE);
        });

        userRepository.saveAll(users);
    }

    @Override
    public void delete(IdsRequest request) {
        List<User> users = userRepository.findByIds(request.getIds());

        request.getIds().forEach(id -> {
            Optional<User> userOptional = users.stream()
                    .filter(user -> Objects.equals(user.getId(), id))
                    .findFirst();

            if (userOptional.isEmpty()) {
                throw new ResponseException(NotFoundError.USER_NOT_FOUND);
            }

            User user = userOptional.get();

            user.setDeleted(Boolean.TRUE);
        });

        userRepository.saveAll(users);
    }

    @Override
    public List<Role> getUserRoles(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseException(NotFoundError.USER_NOT_FOUND));

        List<UserRole> userRoles = userRoleRepository.findByUser(user.getId());

        if (userRoles.isEmpty()) return List.of();

        List<UUID> roleIds = userRoles.stream()
                .map(UserRole::getRoleId)
                .toList();

        return roleRepository.findActiveByIds(roleIds);
    }

    @Override
    public void setUserRole(UUID id, UUID roleId) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseException(NotFoundError.USER_NOT_FOUND));

        Role role = roleRepository.findActiveById(roleId)
                .orElseThrow(() -> new ResponseException(NotFoundError.ROLE_NOT_FOUND));

        UserRole userRole = UserRole.builder()
                .id(IdUtils.nextId())
                .userId(user.getId())
                .roleId(role.getId())
                .deleted(Boolean.FALSE)
                .build();

        userRoleRepository.save(userRole);
    }

}

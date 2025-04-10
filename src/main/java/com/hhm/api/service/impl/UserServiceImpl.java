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
import com.hhm.api.repository.custom.UserRepositoryCustom;
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
    private final UserRepositoryCustom userRepositoryCustom;
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
    public User getUserById(UUID id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()){
            throw new ResponseException(NotFoundError.USER_NOT_FOUND);
        }
        return user.get();
    }

    @Override
    public UserDetailResponse getUserDetailById(UUID id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()){
            throw new ResponseException(NotFoundError.USER_NOT_FOUND);
        }
        User user1 = user.get();
        Optional<UserInformation> optionalUserInformation = userInformationRepository.findById(user1.getId());
        if(optionalUserInformation.isEmpty()){
            throw new ResponseException(NotFoundError.USER_INFORMATION_NOT_FOUND);
        }
        UserInformation userInformation = optionalUserInformation.get();
        List<UserRole> userRoles = userRoleRepository.findByUserId(user1.getId());

        return UserDetailResponse.builder()
                .email(user1.getEmail())
                .deletedUser(user1.getDeleted())
                .version(user1.getVersion())
                .accountType(user1.getAccountType())
                .username(user1.getUsername())
                .avatarUrl(userInformation.getAvatarUrl())
                .address(userInformation.getAddress())
                .dateOfBirth(userInformation.getDateOfBirth())
                .firstName(userInformation.getFirstName())
                .lastName(userInformation.getLastName())
                .middleName(userInformation.getMiddleName())
                .gender(userInformation.getGender())
                .phone(userInformation.getPhone())
                .userRoles(userRoles)
                .build();
    }

    @Override
    public PageDTO<User> search(UserSearchRequest request) {
        Long count = userRepositoryCustom.count(request);
        if(Objects.equals(count,0L)){
            return PageDTO.empty(request.getPageIndex(),request.getPageSize());
        }
        return PageDTO.of(userRepositoryCustom.search(request),request.getPageIndex(),request.getPageSize(),count);
    }

    @Override
    public void active(IdsRequest request) {
        List<User> userList = userRepository.findByIds(request.getIds());
        request.getIds().forEach(id->{
            Optional<User> optionalUser = userList.stream()
                    .filter(user ->Objects.equals(user.getId(),id))
                    .findFirst();
        if(optionalUser.isEmpty()){
            throw new ResponseException(NotFoundError.USER_NOT_FOUND);
        }
        User user = optionalUser.get();
        if(Objects.equals(user.getStatus(),ActiveStatus.ACTIVE)){
            throw new ResponseException(BadRequestError.USER_WAS_ACTIVATED);
        }
        user.setStatus(ActiveStatus.ACTIVE);

        });
       userRepository.saveAll(userList);
    }

    @Override
    public void inactive(IdsRequest request) {
     List<User> userList = userRepository.findByIds(request.getIds());
     request.getIds().forEach(id->{
         Optional<User> user = userList.stream()
                 .filter(user1 -> Objects.equals(user1.getId(),id))
                 .findFirst();
         if(user.isEmpty()){
             throw new ResponseException(NotFoundError.USER_NOT_FOUND);
         }
         User user2 = user.get();
         if(Objects.equals(user2.getStatus(),ActiveStatus.INACTIVE)){
             throw new ResponseException(BadRequestError.USER_WAS_INACTIVATED);
         }
         user2.setStatus(ActiveStatus.INACTIVE);
     });
     userRepository.saveAll(userList);
    }

    @Override
    public void delete(IdsRequest request) {
         List<User> listUser = userRepository.findByIds(request.getIds());
         request.getIds().forEach(id->{
             Optional<User> optionalUser = listUser.stream()
                     .filter(user -> Objects.equals(user.getId(),id))
                     .findFirst();
             if(optionalUser.isEmpty()){
                 throw new ResponseException(NotFoundError.USER_NOT_FOUND);
             }
             User user = optionalUser.get();
             user.setDeleted(Boolean.TRUE);
         });
         userRepository.saveAll(listUser);
    }

}

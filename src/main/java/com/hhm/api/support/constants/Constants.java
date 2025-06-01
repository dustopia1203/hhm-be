package com.hhm.api.support.constants;

import com.hhm.api.support.enums.Permission;
import com.hhm.api.support.enums.ResourceCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

public interface Constants {
    final class CacheName {
        public static final String USER_AUTHORITY = "user-authority";
        public static final String INVALID_ACCESS_TOKEN_CACHE_NAME = "invalid-access-token";
        public static final String INVALID_REFRESH_TOKEN_CACHE_NAME = "invalid-refresh-token";
        public static final String INVALID_REFRESH_TOKEN_LONG_CACHE_NAME = "invalid-refresh-token-long";
        public static final String USER_OTP_CACHE_NAME = "user-otp";
    }

    final class Template {
        public static final String ACTIVE_ACCOUNT_EMAIL_TEMPLATE = "mail/activeAccountEmail";
    }

    @Getter
    @AllArgsConstructor
    enum DefaultRole {
        MANAGER(Map.of(ResourceCode.ALL, List.of(Permission.MANAGE))),
        MEMBER(Map.of(
                ResourceCode.ACCOUNT, List.of(Permission.SELF_CREATE, Permission.SELF_READ, Permission.SELF_UPDATE),
                ResourceCode.SHOP, List.of(Permission.SELF_CREATE, Permission.SELF_UPDATE)
        )),
        ;

        final Map<ResourceCode, List<Permission>> privileges;
    }

    @Getter
    @AllArgsConstructor
    enum DefaultUser {
        ADMIN("admin", "Admin123@", "admin@mail.com", DefaultRole.MANAGER),
        ;

        final String username;
        final String password;
        final String email;
        final DefaultRole defaultRole;
    }

}

package com.hhm.api.config.security;

import com.hhm.api.support.enums.error.AuthorizationError;
import com.hhm.api.support.exception.ResponseException;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import java.io.Serializable;
import java.util.List;

public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        String requiredPermission = (String) permission;

        if (authentication instanceof CustomUserAuthentication userAuthentication) {
            String requiredResourceCode = requiredPermission.split(":")[0].toUpperCase();
            String requiredAction = requiredPermission.split(":")[1].toUpperCase();

            return userAuthentication.getGrantedPermissions().stream()
                    .anyMatch(userPermission -> {
                        String resourceCode = userPermission.split(":")[0].toUpperCase();
                        String action = userPermission.split(":")[1].toUpperCase();

                        return List
                                .of(
                                        resourceCode + ":" + action,
                                        "ALL:" + action,
                                        resourceCode + ":MANAGE",
                                        "ALL:MANAGE"
                                )
                                .contains(requiredResourceCode + ":" + requiredAction);
                    });
        } else {
            throw new ResponseException(AuthorizationError.UNSUPPORTED_AUTHENTICATION);
        }
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}

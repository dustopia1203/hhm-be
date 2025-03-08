package com.hhm.api.support.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;
import java.util.Optional;

public class SecurityUtils {
    public static String getPrincipal(Authentication authentication) {
        if (Objects.isNull(authentication)) return null;

        if (authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        } else {
            return authentication.getPrincipal().toString();
        }
    }

    public static Optional<String> getCurrentUser() {
        return Optional.ofNullable(getPrincipal(SecurityContextHolder.getContext().getAuthentication()));
    }
}

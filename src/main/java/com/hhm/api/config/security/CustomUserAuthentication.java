package com.hhm.api.config.security;

import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
public class CustomUserAuthentication extends UsernamePasswordAuthenticationToken {
    private final UUID userId;
    private final String token;
    private final List<String> grantedPermissions;


    public CustomUserAuthentication(
            Object principal,
            Object credentials,
            Collection<? extends GrantedAuthority> authorities,
            UUID userId,
            String token
    ) {
        super(principal, credentials, authorities);

        this.userId = userId;
        this.token = token;
        this.grantedPermissions = CollectionUtils.isEmpty(authorities) ?
                new ArrayList<>()
                : authorities.stream().map(GrantedAuthority::getAuthority).toList();
    }

    public CustomUserAuthentication(Object principal, Object credentials) {
        super(principal, credentials);

        userId = null;
        token = null;
        grantedPermissions = Collections.emptyList();
    }
}

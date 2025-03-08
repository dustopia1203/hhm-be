package com.hhm.api.config.security;

import com.hhm.api.model.dto.UserAuthority;
import com.hhm.api.service.AuthenticationService;
import com.hhm.api.service.TokenCacheService;
import com.hhm.api.support.enums.error.AuthenticationError;
import com.hhm.api.support.exception.ResponseException;
import com.hhm.api.support.util.IdUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private final TokenCacheService tokenCacheService;
    private final AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String token = extractBearerToken(request);

        if (Objects.isNull(token) || !isValidToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (
                tokenCacheService.isRevokedAccessToken(token)
                        || tokenCacheService.isRevokedRefreshToken(token, false)
                        || tokenCacheService.isRevokedRefreshToken(token, true)
        ) {
            throw new ResponseException(AuthenticationError.INVALID_AUTHENTICATION_TOKEN);
        }

        UUID userId = IdUtils.convertStringToUUID(tokenProvider.extractUserId(token));

        if (Objects.isNull(userId)) throw new ResponseException(AuthenticationError.INVALID_AUTHENTICATION_TOKEN);

        UserAuthority userAuthority = authenticationService.getUserAuthority(userId);

        List<SimpleGrantedAuthority> grantedAuthorities = userAuthority.getGrantedPrivileges().stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        User principal = new User(userAuthority.getUsername(), "", grantedAuthorities);

        CustomUserAuthentication authentication = new CustomUserAuthentication(principal, "", grantedAuthorities, userId, token);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private String extractBearerToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (Objects.nonNull(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }

        return null;
    }

    public boolean isValidToken(String token) {
        Date issuedAt = tokenProvider.extractIssuedAt(token);
        Date expiration = tokenProvider.extractExpiration(token);

        if (Objects.isNull(issuedAt)) return false;

        if (Objects.isNull(expiration) || expiration.before(Date.from(Instant.now()))) return false;

        return true;
    }
}

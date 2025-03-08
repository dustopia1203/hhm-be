package com.hhm.api.service.impl;

import com.hhm.api.service.CacheService;
import com.hhm.api.service.TokenCacheService;
import com.hhm.api.support.constants.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TokenCacheServiceImpl implements TokenCacheService {
    private final CacheService<String, Boolean> cacheService;

    @Override
    public void revokeAccessToken(String token) {
        try {
            cacheService.put(Constants.CacheName.INVALID_ACCESS_TOKEN_CACHE_NAME, token, true);
        } catch (Exception e) {
            log.error("Revoke invalid access token occurred error", e);
        }
    }

    @Override
    public boolean isRevokedAccessToken(String token) {
        try {
            return Optional.ofNullable(cacheService.get(Constants.CacheName.INVALID_ACCESS_TOKEN_CACHE_NAME, token))
                    .orElse(false);
        } catch (Exception e) {
            log.error("Revoke invalid access token occurred error", e);
            return false;
        }
    }

    @Override
    public void revokeRefreshToken(String token, boolean isRememberMe) {
        try {
            if (!isRememberMe) {
                cacheService.put(Constants.CacheName.INVALID_REFRESH_TOKEN_CACHE_NAME, token, true);
            } else {
                cacheService.put(Constants.CacheName.INVALID_REFRESH_TOKEN_LONG_CACHE_NAME, token, true);
            }
        } catch (Exception e) {
            log.error("Revoke invalid access token occurred error", e);
        }
    }

    @Override
    public boolean isRevokedRefreshToken(String token, boolean isRememberMe) {
        try {
            if (!isRememberMe) {
                return Optional.ofNullable(cacheService.get(Constants.CacheName.INVALID_REFRESH_TOKEN_CACHE_NAME, token))
                        .orElse(false);
            } else {
                return Optional.ofNullable(cacheService.get(Constants.CacheName.INVALID_REFRESH_TOKEN_LONG_CACHE_NAME, token))
                        .orElse(false);
            }
        } catch (Exception e) {
            log.error("Revoke invalid access token occurred error", e);
            return false;
        }
    }
}

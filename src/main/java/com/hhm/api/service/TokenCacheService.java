package com.hhm.api.service;

public interface TokenCacheService {
    void revokeAccessToken(String token);

    boolean isRevokedAccessToken(String token);

    void revokeRefreshToken(String token, boolean isRememberMe);

    boolean isRevokedRefreshToken(String token, boolean isRememberMe);
}

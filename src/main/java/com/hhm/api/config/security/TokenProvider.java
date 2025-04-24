package com.hhm.api.config.security;

import com.hhm.api.config.properties.AuthenticationProperties;
import com.hhm.api.support.enums.TokenType;
import com.hhm.api.support.enums.error.AuthenticationError;
import com.hhm.api.support.exception.ResponseException;
import com.hhm.api.support.util.IdUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenProvider {
    private final AuthenticationProperties authenticationProperties;

    public String buildToken(Authentication authentication, UUID userId, TokenType tokenType) {
        Date expiration = Date.from(Instant.now());

        switch (tokenType) {
            case ACCESS_TOKEN -> expiration = Date.from(Instant.now().plus(authenticationProperties.getAccessTokenExpiresIn()));
            case REFRESH_TOKEN -> expiration = Date.from(Instant.now().plus(authenticationProperties.getRefreshTokenExpiresIn()));
            case REFRESH_TOKEN_LONG -> expiration = Date.from(Instant.now().plus(authenticationProperties.getRefreshTokenLongExpiresIn()));
        }

        return Jwts
                .builder()
                .id(IdUtils.convertUUIDToString(userId))
                .subject(authentication.getName())
                .signWith(secretKey())
                .issuedAt(Date.from(Instant.now()))
                .expiration(expiration)
                .compact();
    }

    public String extractSubject(String token) {
        Claims claims = extractClaims(token);

        if (Objects.isNull(claims)) return null;

        return claims.getSubject();
    }

    public String extractUserId(String token) {
        Claims claims = extractClaims(token);

        if (Objects.isNull(claims)) return null;

        return claims.getId();
    }

    public Date extractIssuedAt(String token) {
        Claims claims = extractClaims(token);

        if (Objects.isNull(claims)) return null;

        return claims.getIssuedAt();
    }

    public Date extractExpiration(String token) {
        Claims claims = extractClaims(token);

        if (Objects.isNull(claims)) return null;

        return claims.getExpiration();
    }

    private SecretKey secretKey() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(authenticationProperties.getSecretKey()));
    }

    private Claims extractClaims(String token) {
        try {
            return Jwts
                    .parser()
                    .verifyWith(secretKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (io.jsonwebtoken.security.SignatureException | MalformedJwtException e) {
            log.info("Invalid JWT signature");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        }

        return null;
    }
}

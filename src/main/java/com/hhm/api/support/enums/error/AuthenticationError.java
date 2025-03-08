package com.hhm.api.support.enums.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthenticationError implements ResponseError {
    INVALID_AUTHENTICATION_TOKEN(401001, "Invalid authentication token"),
    TOKEN_WAS_REVOKED(401002, "Token was revoked"),
    ;

    private final int code;
    private final String message;

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public int getStatus() {
        return 401;
    }

    @Override
    public int getCode() {
        return code;
    }
}

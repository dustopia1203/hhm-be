package com.hhm.api.support.enums.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthorizationError implements ResponseError {
    ACCESS_DENIED(401001, "Access denied"),
    UNAUTHORIZED(401002, "Unauthorized"),
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

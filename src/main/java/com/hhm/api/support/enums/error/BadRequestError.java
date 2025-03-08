package com.hhm.api.support.enums.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BadRequestError implements ResponseError {
    LOGIN_FAILED(400001, "Login failed"),
    USERNAME_EXISTED(400002, "Username existed"),
    EMAIL_EXISTED(400003, "Email existed"),
    USER_WAS_INACTIVATED(400004, "User was inactivated"),
    USER_WAS_ACTIVATED(400005, "User was activated"),
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
        return 400;
    }

    @Override
    public int getCode() {
        return code;
    }
}

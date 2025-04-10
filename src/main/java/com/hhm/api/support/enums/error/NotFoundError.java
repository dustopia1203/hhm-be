package com.hhm.api.support.enums.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NotFoundError implements ResponseError {
    USER_NOT_FOUND(404001, "User not found"),
    CATEGORY_NOT_FOUND(404002, "Category not found"),
    SHOP_NOT_FOUND(404003, "Shop not found"),
    PRODUCT_NOT_FOUND(404004, "Product not found"),
    USER_INFORMATION_NOT_FOUND(404005,"User information not found"),

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
        return 404;
    }

    @Override
    public int getCode() {
        return code;
    }
}

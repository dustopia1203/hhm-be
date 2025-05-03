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
    INVALID_PARENT_CATEGORY(400006, "Invalid parent category"),
    CATEGORY_WAS_ACTIVATED(400007, "Category was activated"),
    CATEGORY_WAS_INACTIVATED(400008, "Category was inactivated"),
    SHOP_WAS_ACTIVATED(400009, "Shop was activated"),
    SHOP_WAS_INACTIVATED(400010, "Shop was inactivated"),
    PRODUCT_WAS_ACTIVATED(400011, "Product was activated"),
    PRODUCT_WAS_INACTIVATED(400012, "Product was inactivated"),
    USER_ALREADY_HAS_SHOP(400013, "User already has shop"),
    SHIPPING_WAS_ACTIVATED(400014, "Shipping was activated"),
    SHIPPING_WAS_INACTIVATED(400014, "Shipping was inactivated"),
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

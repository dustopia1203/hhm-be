package com.hhm.api.model.dto.mapper;

import com.hhm.api.model.dto.UserAuthority;
import com.hhm.api.model.dto.response.CartItemResponse;
import com.hhm.api.model.dto.response.OrderItemResponse;
import com.hhm.api.model.dto.response.ProductResponse;
import com.hhm.api.model.dto.response.ProfileResponse;
import com.hhm.api.model.dto.response.ShopDetailResponse;
import com.hhm.api.model.entity.CartItem;
import com.hhm.api.model.entity.OrderItem;
import com.hhm.api.model.entity.Product;
import com.hhm.api.model.entity.Shop;
import com.hhm.api.model.entity.UserInformation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AutoMapper {
    ProfileResponse toResponse(UserAuthority userAuthority, UserInformation userInformation);

    ShopDetailResponse toResponse(Shop shop);

    ProductResponse toResponse(Product product);

    CartItemResponse toResponse(CartItem cartItem);

    OrderItemResponse toResponse(OrderItem orderItem);
}

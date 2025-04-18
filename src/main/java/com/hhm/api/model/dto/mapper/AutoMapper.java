package com.hhm.api.model.dto.mapper;

import com.hhm.api.model.dto.UserAuthority;
import com.hhm.api.model.dto.response.ProfileResponse;
import com.hhm.api.model.dto.response.ShopDetailResponse;
import com.hhm.api.model.entity.Shop;
import com.hhm.api.model.entity.UserInformation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AutoMapper {
    ProfileResponse toResponse(UserAuthority userAuthority, UserInformation userInformation);

    ShopDetailResponse toResponse(Shop shop);
}

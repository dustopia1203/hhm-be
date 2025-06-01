package com.hhm.api.model.dto.response;

import com.hhm.api.support.enums.ActiveStatus;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class ShopDetailResponse {
    private UUID id;
    private String name;
    private String address;
    private String avatarUrl;
    private Long productCount;
    private Long reviewCount;
    private Float rating;
    private ActiveStatus status;
    private Instant createdAt;
}

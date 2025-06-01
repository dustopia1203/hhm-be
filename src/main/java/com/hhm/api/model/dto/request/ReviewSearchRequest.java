package com.hhm.api.model.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReviewSearchRequest extends PagingRequest {
    private List<UUID> userIds;
    private List<UUID> shopIds;
    private List<UUID> orderItemIds;
    private List<UUID> productIds;
    private Float rating;
}

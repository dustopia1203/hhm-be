package com.hhm.api.model.dto.request;

import com.hhm.api.support.enums.ActiveStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class ShopSearchRequest extends PagingRequest {
    private List<UUID> userIds;
    private ActiveStatus status;
}

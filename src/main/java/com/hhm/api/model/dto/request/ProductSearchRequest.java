package com.hhm.api.model.dto.request;

import com.hhm.api.support.enums.ActiveStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProductSearchRequest extends PagingRequest {
    private List<UUID> ids;
    private List<UUID> shopIds;
    private List<UUID> categoryIds;
    private ActiveStatus status;
}

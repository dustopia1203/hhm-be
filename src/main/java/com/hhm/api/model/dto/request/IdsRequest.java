package com.hhm.api.model.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class IdsRequest extends Request {
    private List<UUID> ids;
}

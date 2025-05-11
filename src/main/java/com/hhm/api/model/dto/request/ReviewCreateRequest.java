package com.hhm.api.model.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class ReviewCreateRequest extends Request {
    private UUID orderItemId;
    private Float rating;
    private String description;
    private String contentUrls;
}

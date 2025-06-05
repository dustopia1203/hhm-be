package com.hhm.api.model.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReviewResponse {
    private String userAvatar;
    private String username;
    private Float rating;
    private String description;
    private String[] images;
    private String createdAt;
}

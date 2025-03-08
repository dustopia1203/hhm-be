package com.hhm.api.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class UserAuthority {
    private UUID userId;
    private String username;
    private List<String> grantedPrivileges;
}

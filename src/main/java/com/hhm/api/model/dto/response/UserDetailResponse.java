package com.hhm.api.model.dto.response;

import com.hhm.api.model.entity.UserRole;
import com.hhm.api.support.enums.AccountType;
import com.hhm.api.support.enums.ActiveStatus;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;


import java.time.Instant;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@SuperBuilder
public class UserDetailResponse extends ProfileResponse {
    private String email;
    private ActiveStatus status;
    private AccountType accountType;
    private List<UserRole> userRoles;
    private Instant createdAt;
    private Instant lastModifiedAt;
}

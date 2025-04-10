package com.hhm.api.model.dto.request;

import com.hhm.api.support.enums.AccountType;
import com.hhm.api.support.enums.ActiveStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserSearchRequest extends PagingRequest {
    private ActiveStatus status;
    private AccountType accountType;
}

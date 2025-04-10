package com.hhm.api.model.dto.response;

import com.hhm.api.model.entity.UserRole;
import com.hhm.api.support.enums.AccountType;
import com.hhm.api.support.enums.ActiveStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@SuperBuilder
public class UserDetailResponse extends ProfileResponse{
      private String email;
      private ActiveStatus status;
      private AccountType accountType;
      private Boolean deletedUser;
      private Long version;
      private List<UserRole> userRoles;
}

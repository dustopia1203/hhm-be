package com.hhm.api.model.dto.response;

import com.hhm.api.model.entity.UserInformation;
import com.hhm.api.support.enums.Gender;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@Builder
public class ProfileResponse {
    private String username;
    private List<String> grantedPrivileges;
    private UserInformation userInformation;
    private String firstName;
    private String lastName;
    private String middleName;
    private String phone;
    private Instant dateOfBirth;
    private String avatarUrl;
    private Gender gender;
    private String address;
}

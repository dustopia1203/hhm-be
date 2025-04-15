package com.hhm.api.model.dto.response;

import com.hhm.api.support.enums.Gender;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.List;

@Data
@SuperBuilder
public class ProfileResponse {
    private String username;
    private List<String> grantedPrivileges;
    private String firstName;
    private String lastName;
    private String middleName;
    private String phone;
    private Instant dateOfBirth;
    private String avatarUrl;
    private Gender gender;
    private String address;
}

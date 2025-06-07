package com.hhm.api.model.dto.request;

import com.hhm.api.support.enums.Gender;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
@Data
@Builder
public class UserInformationUpdateRequest {
    private String firstName;
    private String lastName;
    private String middleName;
    private String phone;
    private Instant dateOfBirth;
    private String avatarUrl;
    private Gender gender;
    private String address;
}

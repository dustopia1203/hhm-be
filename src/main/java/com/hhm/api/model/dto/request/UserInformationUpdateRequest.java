package com.hhm.api.model.dto.request;

import com.hhm.api.support.enums.Gender;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Date;

@Data
@Builder
public class UserInformationUpdateRequest {
    private String firstName;
    private String lastName;
    private String middleName;
    private String phone;
    private Date dateOfBirth;
    private String avatarUrl;
    private Gender gender;
    private String address;
}
package com.hhm.api.model.dto.request;

import com.hhm.api.support.constants.ValidateConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class AuthenticateRequest extends Request {
    @NotBlank(message = "CREDENTIAL_REQUIRED")
    @Size(
            max = ValidateConstraint.LENGTH.USERNAME_MAX_LENGTH,
            message = "USERNAME_LENGTH"
    )
    private String credential;

    @NotBlank(message = "PASSWORD_REQUIRED")
    @Size(
            min = ValidateConstraint.LENGTH.PASSWORD_MIN_LENGTH,
            max = ValidateConstraint.LENGTH.PASSWORD_MAX_LENGTH,
            message = "PASSWORD_LENGTH"
    )
    private String password;

    private Boolean rememberMe;
}

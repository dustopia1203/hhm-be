package com.hhm.api.model.dto.request;

import com.hhm.api.support.constants.ValidateConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RegisterRequest extends Request {
    @NotBlank(message = "USERNAME_REQUIRED")
    @Size(
            max = ValidateConstraint.Length.USERNAME_MAX_LENGTH,
            min = ValidateConstraint.Length.USERNAME_MIN_LENGTH,
            message = "USERNAME_LENGTH"
    )
    @Pattern(regexp = ValidateConstraint.Format.USERNAME_PATTERN, message = "USERNAME_FORMAT")
    private String username;

    @NotBlank(message = "EMAIL_REQUIRED")
    @Size(max = ValidateConstraint.Length.EMAIL_MAX_LENGTH, message = "EMAIL_LENGTH")
    @Pattern(regexp = ValidateConstraint.Format.EMAIL_PATTERN, message = "EMAIL_FORMAT")
    private String email;

    @NotBlank(message = "PASSWORD_REQUIRED")
    @Size(
            min = ValidateConstraint.Length.PASSWORD_MIN_LENGTH,
            max = ValidateConstraint.Length.PASSWORD_MAX_LENGTH,
            message = "PASSWORD_LENGTH"
    )
    @Pattern(
            regexp = ValidateConstraint.Format.PASSWORD_REGEX,
            message = "PASSWORD_FORMAT"
    )
    private String password;
}

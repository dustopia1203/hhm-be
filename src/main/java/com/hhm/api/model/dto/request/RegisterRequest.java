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
    @NotBlank(message = "Username required")
    @Size(
            max = ValidateConstraint.Length.USERNAME_MAX_LENGTH,
            min = ValidateConstraint.Length.USERNAME_MIN_LENGTH,
            message = "Username length"
    )
    @Pattern(regexp = ValidateConstraint.Format.USERNAME_PATTERN, message = "Username format")
    private String username;

    @NotBlank(message = "Email required")
    @Size(max = ValidateConstraint.Length.EMAIL_MAX_LENGTH, message = "Username length")
    @Pattern(regexp = ValidateConstraint.Format.EMAIL_PATTERN, message = "EMAIL_WRONG_FORMAT")
    private String email;

    @NotBlank(message = "Password required")
    @Size(
            min = ValidateConstraint.Length.PASSWORD_MIN_LENGTH,
            max = ValidateConstraint.Length.PASSWORD_MAX_LENGTH,
            message = "PASSWORD_LENGTH"
    )
    @Pattern(
            regexp = ValidateConstraint.Format.PASSWORD_REGEX,
            message = "FORMAT_PASSWORD"
    )
    private String password;
}

package com.hhm.api.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResetPasswordRequest extends Request {
    @NotBlank(message = "CREDENTIAL_REQUIRED")
    private String credential;
    @NotBlank(message = "PASSWORD_REQUIRED")
    private String newPassword;
}

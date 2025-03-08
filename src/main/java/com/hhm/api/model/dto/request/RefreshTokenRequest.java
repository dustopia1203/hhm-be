package com.hhm.api.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class RefreshTokenRequest extends Request {
    @NotBlank(message = "Refresh token required")
    private String refreshToken;

    private Boolean rememberMe;
}

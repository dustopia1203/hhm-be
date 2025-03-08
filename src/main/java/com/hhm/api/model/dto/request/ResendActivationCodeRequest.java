package com.hhm.api.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResendActivationCodeRequest extends Request {
    @NotBlank(message = "Credential required")
    private String credential;
}

package com.hhm.api.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ActiveAccountRequest extends Request {
    @NotBlank(message = "Credential required")
    private String credential;
    @NotBlank(message = "Activation code required")
    private String activationCode;
}

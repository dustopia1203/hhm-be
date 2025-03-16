package com.hhm.api.model.dto.response;

import com.hhm.api.model.InvalidFieldError;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class InvalidInputResponse extends ErrorResponse<Void> {
    private List<InvalidFieldError> invalidFieldErrors;

    public InvalidInputResponse(int code, String message, String error, List<InvalidFieldError> invalidFieldErrors) {
        super(code, message, error);

        this.invalidFieldErrors = invalidFieldErrors;
    }
}

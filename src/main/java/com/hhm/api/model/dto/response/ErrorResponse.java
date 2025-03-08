package com.hhm.api.model.dto.response;

import com.hhm.api.support.enums.ResponseStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ErrorResponse<T> extends Response<T> {
    private String error;

    public ErrorResponse(int code, String message, String error) {
        this.setSuccess(false);
        this.setCode(code);
        this.setMessage(message);
        this.setStatus(ResponseStatus.FAILED.name());
        this.error = error;
    }
}

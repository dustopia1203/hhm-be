package com.hhm.api.model.dto.response;

import com.hhm.api.support.enums.ResponseStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
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

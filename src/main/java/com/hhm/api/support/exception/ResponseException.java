package com.hhm.api.support.exception;

import com.hhm.api.support.enums.error.ResponseError;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResponseException extends RuntimeException {
    private final ResponseError error;

    public ResponseException(ResponseError error) {
        super(error.getMessage());
        this.error = error;
    }
}

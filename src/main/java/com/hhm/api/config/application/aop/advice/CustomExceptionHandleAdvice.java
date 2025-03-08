package com.hhm.api.config.application.aop.advice;

import com.hhm.api.support.enums.error.InternalServerError;
import com.hhm.api.support.enums.error.ResponseError;
import com.hhm.api.model.dto.response.ErrorResponse;
import com.hhm.api.support.exception.ResponseException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandleAdvice {


    @ExceptionHandler({ResponseException.class})
    public ResponseEntity<ErrorResponse<Object>> handleResponseException(ResponseException e, HttpServletRequest request) {
        log.warn("Failed to handle request {}: {}", request.getRequestURI(), e.getError().getMessage(), e);
        ResponseError error = e.getError();
        return ResponseEntity
                .status(error.getStatus())
                .body(
                        new ErrorResponse<>(
                                error.getCode(),
                                error.getMessage(),
                                error.getName()
                        )
                );
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse<Void>> handleResponseException(Exception e, HttpServletRequest request) {
        ResponseError error = InternalServerError.INTERNAL_SERVER_ERROR;
        log.error("Failed to handle request {}: {}", request.getRequestURI(), error.getMessage(), e);
        return ResponseEntity.status(error.getStatus()).body(
                new ErrorResponse<>(
                        error.getCode(),
                        error.getMessage(),
                        error.getName()
                )
        );
    }
}

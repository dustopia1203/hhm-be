package com.hhm.api.config.application.aop.advice;

import com.hhm.api.model.dto.response.ErrorResponse;
import com.hhm.api.support.enums.error.AuthorizationError;
import com.hhm.api.support.enums.error.InternalServerError;
import com.hhm.api.support.enums.error.ResponseError;
import com.hhm.api.support.exception.ResponseException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.support.RequestContextUtils;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class CustomExceptionHandleAdvice {
    private final MessageSource messageSource;

    @ExceptionHandler({ResponseException.class})
    public ResponseEntity<ErrorResponse<Object>> handleResponseException(ResponseException e, HttpServletRequest request) {
        log.warn("Failed to handle request {}: {}", request.getRequestURI(), e.getError().getMessage(), e);
        ResponseError error = e.getError();

        return ResponseEntity
                .status(error.getStatus())
                .body(
                        new ErrorResponse<>(
                                error.getCode(),
                                messageSource.getMessage(error.getName(), null, LocaleContextHolder.getLocale()),
                                error.getName()
                        )
                );
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ErrorResponse<Void>> handleValidationException(AccessDeniedException e, HttpServletRequest request) {
        log.warn("Failed to handle {} request {}: {}", request.getMethod(), request.getRequestURI(), e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(
                        new ErrorResponse<>(
                                AuthorizationError.ACCESS_DENIED.getCode(),
                                messageSource.getMessage(AuthorizationError.ACCESS_DENIED.getName(), null, LocaleContextHolder.getLocale()),
                                AuthorizationError.ACCESS_DENIED.getName()
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
                        messageSource.getMessage(error.getName(), null, LocaleContextHolder.getLocale()),
                        error.getName()
                )
        );
    }
}

package com.hhm.api.presentation.web.rest;

import com.hhm.api.model.dto.request.ActiveAccountRequest;
import com.hhm.api.model.dto.request.AuthenticateRequest;
import com.hhm.api.model.dto.request.RefreshTokenRequest;
import com.hhm.api.model.dto.request.RegisterRequest;
import com.hhm.api.model.dto.request.ResendActivationCodeRequest;
import com.hhm.api.model.dto.response.AuthenticateResponse;
import com.hhm.api.model.dto.response.Response;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@RequestMapping("/api/account")
public interface AccountController {
    @PostMapping("/register")
    Response<Boolean> register(@Valid @RequestBody RegisterRequest request);

    @PostMapping("/resend-code")
    Response<Boolean> resendActivationCode(@Valid @RequestBody ResendActivationCodeRequest request);

    @PostMapping("/active")
    Response<Boolean> activeByCode(@Valid @RequestBody ActiveAccountRequest request);

    @PostMapping("/{id}/active")
    Response<Boolean> activeById(@PathVariable UUID id);

    @PostMapping("/authenticate")
    Response<AuthenticateResponse> login(@Valid @RequestBody AuthenticateRequest request);

    @PostMapping("/refresh-token")
    Response<AuthenticateResponse> refreshToken(@RequestBody RefreshTokenRequest request);
}

package com.hhm.api.presentation.web.rest;

import com.hhm.api.model.dto.request.ActiveAccountRequest;
import com.hhm.api.model.dto.request.AuthenticateRequest;
import com.hhm.api.model.dto.request.RefreshTokenRequest;
import com.hhm.api.model.dto.request.RegisterRequest;
import com.hhm.api.model.dto.request.ResendActivationCodeRequest;
import com.hhm.api.model.dto.response.AuthenticateResponse;
import com.hhm.api.model.dto.response.ProfileResponse;
import com.hhm.api.model.dto.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Tag(name = "Account Resources")
@RequestMapping("/api/account")
public interface AccountController {
    @Operation(summary = "Register new user")
    @PostMapping("/register")
    Response<Boolean> register(@Valid @RequestBody RegisterRequest request);

    @Operation(summary = "Resend activation code")
    @PostMapping("/resend-code")
    Response<Boolean> resendActivationCode(@Valid @RequestBody ResendActivationCodeRequest request);

    @Operation(summary = "Active user account by activation code")
    @PostMapping("/active")
    Response<Boolean> activeByCode(@Valid @RequestBody ActiveAccountRequest request);

    @Operation(summary = "Active user account by id")
    @PostMapping("/{id}/active")
    Response<Boolean> activeById(@PathVariable UUID id);

    @Operation(summary = "Login")
    @PostMapping("/authenticate")
    Response<AuthenticateResponse> login(@Valid @RequestBody AuthenticateRequest request);

    @Operation(summary = "Refresh access token")
    @PostMapping("/refresh-token")
    Response<AuthenticateResponse> refreshToken(@RequestBody RefreshTokenRequest request);

    @Operation(summary = "Get account profile")
    @GetMapping("/profile")
    Response<ProfileResponse> getAccountProfile();
}

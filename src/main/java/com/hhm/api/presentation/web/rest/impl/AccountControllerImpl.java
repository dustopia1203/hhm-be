package com.hhm.api.presentation.web.rest.impl;

import com.hhm.api.model.dto.request.ActiveAccountRequest;
import com.hhm.api.model.dto.request.AuthenticateRequest;
import com.hhm.api.model.dto.request.RefreshTokenRequest;
import com.hhm.api.model.dto.request.RegisterRequest;
import com.hhm.api.model.dto.request.ResendActivationCodeRequest;
import com.hhm.api.model.dto.response.AuthenticateResponse;
import com.hhm.api.model.dto.response.ProfileResponse;
import com.hhm.api.model.dto.response.Response;
import com.hhm.api.model.entity.User;
import com.hhm.api.presentation.web.rest.AccountController;
import com.hhm.api.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AccountControllerImpl implements AccountController {
    private final AccountService accountService;

    @Override
    public Response<Boolean> register(RegisterRequest request) {
        accountService.register(request);
        return Response.ok();
    }

    @Override
    public Response<Boolean> resendActivationCode(ResendActivationCodeRequest request) {
        accountService.resendActivationCode(request);
        return Response.ok();
    }

    @Override
    public Response<Boolean> activeByCode(ActiveAccountRequest request) {
        accountService.activeAccount(request);
        return Response.ok();
    }

    @Override
    public Response<Boolean> activeById(UUID id) {
        accountService.activeAccount(id);
        return Response.ok();
    }

    @Override
    public Response<AuthenticateResponse> login(AuthenticateRequest request) {
        return Response.of(accountService.login(request));
    }

    @Override
    public Response<AuthenticateResponse> refreshToken(RefreshTokenRequest request) {
        return Response.of(accountService.refreshToken(request));
    }

    @Override
    public Response<ProfileResponse> getAccountProfile() {
        return Response.of(accountService.getAccountProfile());
    }

    @Override
    public Response<AuthenticateResponse> loginGoogle(String code) throws IOException {
        return Response.of(accountService.loginGoogle(code));
    }

    @Override
    public Response<AuthenticateResponse> loginFacebook(String code)  {
        return Response.of(accountService.loginFacebook(code));
    }
}

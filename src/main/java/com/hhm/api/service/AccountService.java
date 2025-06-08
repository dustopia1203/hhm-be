package com.hhm.api.service;

import com.hhm.api.model.dto.request.ActiveAccountRequest;
import com.hhm.api.model.dto.request.AuthenticateRequest;
import com.hhm.api.model.dto.request.RefreshTokenRequest;
import com.hhm.api.model.dto.request.RegisterRequest;
import com.hhm.api.model.dto.request.ResendActivationCodeRequest;


import com.hhm.api.model.dto.request.ResetPasswordRequest;

import com.hhm.api.model.dto.request.UserInformationUpdateRequest;

import com.hhm.api.model.dto.response.AccountBalanceResponse;

import com.hhm.api.model.dto.response.AuthenticateResponse;
import com.hhm.api.model.dto.response.ProfileResponse;
import com.hhm.api.model.entity.User;

import java.io.IOException;
import java.util.UUID;

public interface AccountService {
    void register(RegisterRequest request);

    void resendActivationCode(ResendActivationCodeRequest request);

    void activeAccount(ActiveAccountRequest request);

    void activeAccount(UUID id);

    AuthenticateResponse login(AuthenticateRequest request);

    AuthenticateResponse refreshToken(RefreshTokenRequest request);

    ProfileResponse getAccountProfile();


    void forgotPassword(ResendActivationCodeRequest request);

    void verifyResetOtp(ActiveAccountRequest request);

    void resetPassword(ResetPasswordRequest request);

    AccountBalanceResponse getAccountBalance();

    AuthenticateResponse loginGoogle(String code) throws IOException;

    void updateProfile(UserInformationUpdateRequest request);

}

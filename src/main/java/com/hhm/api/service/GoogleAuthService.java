package com.hhm.api.service;

import com.hhm.api.model.dto.response.AuthenticateResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Service
public interface GoogleAuthService  {
    AuthenticateResponse handleGoogleCallback(String code, String state, String sessionState, boolean rememberMe)throws IOException;

    String getGoogleAuthUrl(String state);
}

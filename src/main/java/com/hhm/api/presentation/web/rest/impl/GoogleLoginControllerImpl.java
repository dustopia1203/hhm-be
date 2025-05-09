package com.hhm.api.presentation.web.rest.impl;


import com.hhm.api.model.dto.response.AuthenticateResponse;
import com.hhm.api.model.dto.response.Response;
import com.hhm.api.presentation.web.rest.GoogleLoginController;
import com.hhm.api.service.GoogleAuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class GoogleLoginControllerImpl implements GoogleLoginController {

    private final GoogleAuthService googleAuthService;

    @Override
    public Response<Map<String, String>> getGoogleAuthUrl(HttpSession session) {

        String state = UUID.randomUUID().toString();

        session.setAttribute("state", state);

        String authUrl = googleAuthService.getGoogleAuthUrl(state);

        Map<String, String> response = new HashMap<>();

        response.put("auth_url", authUrl);

        return Response.of(response);
    }

    @Override
    public Response<AuthenticateResponse> handleGoogleCallback(
            @RequestParam("code") String code,
            @RequestParam("state") String state,
            @RequestParam(value = "rememberMe", defaultValue = "false") boolean rememberMe,
            HttpSession session) throws IOException {

        String sessionState = (String) session.getAttribute("state");

        AuthenticateResponse response = googleAuthService.handleGoogleCallback(code, state, sessionState, rememberMe);

        return Response.of(response);
    }
}
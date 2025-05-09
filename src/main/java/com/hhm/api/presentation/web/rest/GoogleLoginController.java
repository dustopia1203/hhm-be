package com.hhm.api.presentation.web.rest;

import com.hhm.api.model.dto.response.AuthenticateResponse;
import com.hhm.api.model.dto.response.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Map;

@Tag(name = "Google Login")
@RequestMapping("/api/account/authenticate")
@Validated
public interface GoogleLoginController {
    @Operation(summary = "Direct Google")
    @GetMapping("/google")
    Response<Map<String, String>> getGoogleAuthUrl(HttpSession session);

    @Operation(summary = "Google Callback ")
    @GetMapping("/callback")
    Response<AuthenticateResponse> handleGoogleCallback(
            @RequestParam("code") String code,
            @RequestParam("state") String state,
            @RequestParam(value = "rememberMe", defaultValue = "false") boolean rememberMe,
            HttpSession session) throws IOException;
}

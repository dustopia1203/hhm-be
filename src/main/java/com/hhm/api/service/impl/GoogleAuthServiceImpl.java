package com.hhm.api.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hhm.api.config.properties.AuthenticationProperties;
import com.hhm.api.config.security.TokenProvider;
import com.hhm.api.model.dto.response.AuthenticateResponse;
import com.hhm.api.model.entity.User;
import com.hhm.api.repository.UserRepository;
import com.hhm.api.service.GoogleAuthService;
import com.hhm.api.support.enums.AccountType;
import com.hhm.api.support.enums.ActiveStatus;
import com.hhm.api.support.util.IdUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Data
public class GoogleAuthServiceImpl implements GoogleAuthService {

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final AuthenticationProperties authenticationProperties;
    private final ObjectMapper objectMapper;

    @Value("${google.client-id}")
    private String clientId;

    @Value("${google.client-secret}")
    private String clientSecret;

    @Value("${google.redirect-uri}")
    private String redirectUri;

    @Value("${google.auth-uri}")
    private String authUri;

    @Value("${google.token-uri}")
    private String tokenUri;

    @Value("${google.user-info-uri}")
    private String userInfoUri;

    @Value("${google.scope}")
    private String scope;

    public String getGoogleAuthUrl(String state) {
        String url = authUri + "?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code" +
                "&scope=" + scope +
                "&access_type=offline" +
                "&state=" + state;

        return url;
    }

    public AuthenticateResponse handleGoogleCallback(String code, String state, String sessionState, boolean rememberMe) throws IOException {
        if (sessionState == null || !sessionState.equals(state)) {
            throw new IllegalArgumentException("Invalid state parameter");
        }

        Map<String, Object> tokenResponse = exchangeCodeForToken(code);

        String IdToken = (String) tokenResponse.get("id_token");

        String accessToken = (String) tokenResponse.get("access_token");

        Number expNum = (Number) tokenResponse.get("expires_in");

        if (accessToken == null) {
            throw new RuntimeException("Failed to obtain access token");
        }


        Map<String, Object> userInfo = getUserInfo(accessToken);

        String email = (String) userInfo.get("email");

        String name = (String) userInfo.get("name");

        if (email == null || email.trim().isEmpty()) {
            throw new RuntimeException("Email is required but missing in user info");
        }


        User user = userRepository.findByEmail(email).orElse(null);

        if (user != null) {
            user.setUsername(name);
        } else {
            user = User.builder()
                    .id(IdUtils.nextId())
                    .email(email)
                    .username(name)
                    .password("google-oauth-" + UUID.randomUUID().toString())
                    .status(ActiveStatus.ACTIVE)
                    .accountType(AccountType.GOOGLE)
                    .deleted(Boolean.FALSE)
                    .build();
        }
        userRepository.save(user);

        Duration accessTokenExpiresIn = Duration.ofSeconds(expNum.longValue());

        Instant accessTokenExpiresAt = Instant.now().plus(accessTokenExpiresIn);

        return AuthenticateResponse.builder()
                .accessToken(IdToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.toSeconds())
                .accessTokenExpiredAt(accessTokenExpiresAt)
                .build();
    }

    private Map<String, Object> exchangeCodeForToken(String code) throws IOException {
        URL url = new URL(tokenUri);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");

        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        conn.setDoOutput(true);

        Map<String, String> params = new HashMap<>();
        params.put("code", code);

        params.put("client_id", clientId);

        params.put("client_secret", clientSecret);

        params.put("redirect_uri", redirectUri);

        params.put("grant_type", "authorization_code");

        String body = params.entrySet().stream()
                .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes(StandardCharsets.UTF_8));

            os.flush();
        }
        String response = readResponse(conn);

        return parseJson(response);
    }

    private Map<String, Object> getUserInfo(String accessToken) throws IOException {
        URL url = new URL(userInfoUri);

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");

        conn.setRequestProperty("Authorization", "Bearer " + accessToken);

        String response = readResponse(conn);

        return parseJson(response);
    }

    private String readResponse(HttpURLConnection conn) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(

                conn.getResponseCode() >= 400 ? conn.getErrorStream() : conn.getInputStream()))) {

            return br.lines().collect(Collectors.joining());
        }
    }

    private Map<String, Object> parseJson(String json) throws IOException {
        System.out.println("Parsing JSON: " + json);
        try {

            return objectMapper.readValue(json, Map.class);
        } catch (Exception e) {

            throw new IOException("Failed to parse JSON response", e);
        }
    }


}
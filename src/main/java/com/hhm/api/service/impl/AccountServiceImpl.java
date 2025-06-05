package com.hhm.api.service.impl;

import com.hhm.api.config.properties.AuthenticationProperties;
import com.hhm.api.config.security.CustomUserAuthentication;
import com.hhm.api.config.security.TokenProvider;
import com.hhm.api.model.dto.UserAuthority;
import com.hhm.api.model.dto.mapper.AutoMapper;
import com.hhm.api.model.dto.request.ActiveAccountRequest;
import com.hhm.api.model.dto.request.AuthenticateRequest;
import com.hhm.api.model.dto.request.RefreshTokenRequest;
import com.hhm.api.model.dto.request.RegisterRequest;
import com.hhm.api.model.dto.request.ResendActivationCodeRequest;
import com.hhm.api.model.dto.response.AccountBalanceResponse;
import com.hhm.api.model.dto.response.AuthenticateResponse;
import com.hhm.api.model.dto.response.ProfileResponse;
import com.hhm.api.model.entity.Role;
import com.hhm.api.model.entity.User;
import com.hhm.api.model.entity.UserInformation;
import com.hhm.api.model.entity.UserRole;
import com.hhm.api.repository.RoleRepository;
import com.hhm.api.repository.TransactionRepository;
import com.hhm.api.repository.UserInformationRepository;
import com.hhm.api.repository.UserRepository;
import com.hhm.api.repository.UserRoleRepository;
import com.hhm.api.service.AccountService;
import com.hhm.api.service.AuthenticationService;
import com.hhm.api.service.CacheService;
import com.hhm.api.service.EmailService;
import com.hhm.api.service.TokenCacheService;
import com.hhm.api.support.constants.Constants;
import com.hhm.api.support.enums.AccountType;
import com.hhm.api.support.enums.ActiveStatus;
import com.hhm.api.support.enums.TokenType;
import com.hhm.api.support.enums.error.AuthenticationError;
import com.hhm.api.support.enums.error.BadRequestError;
import com.hhm.api.support.enums.error.NotFoundError;
import com.hhm.api.support.exception.ResponseException;
import com.hhm.api.support.util.IdUtils;
import com.hhm.api.support.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final TokenProvider tokenProvider;
    private final TokenCacheService tokenCacheService;
    private final AuthenticationProperties authenticationProperties;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final CacheService<String, String> cacheService;
    private final EmailService emailService;
    private final AuthenticationService authenticationService;
    private final UserInformationRepository userInformationRepository;
    private final AutoMapper autoMapper;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;

    private final RestTemplate restTemplate;

    @Value("${google.client-id}")
    private String clientId;
    @Value("${google.client-secret}")
    private String clientSecret;
    @Value("${google.redirect-url}")
    private String redirectUrl;
    @Value("${google.token-url}")
    private String tokenUrl;
    @Value("${google.user-info-url}")
    private String userInforUrl;

    private final TransactionRepository transactionRepository;

    @Override
    public void register(RegisterRequest request) {
        Optional<User> userOptional = userRepository.findSystemByEmail(request.getEmail());

        if (userOptional.isPresent()) {
            throw new ResponseException(BadRequestError.EMAIL_EXISTED);
        }

        userOptional = userRepository.findSystemByUsername(request.getUsername());

        if (userOptional.isPresent()) {
            throw new ResponseException(BadRequestError.USERNAME_EXISTED);
        }

        User user = User.builder()
                .id(IdUtils.nextId())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .status(ActiveStatus.INACTIVE)
                .accountType(AccountType.SYSTEM)
                .deleted(Boolean.FALSE)
                .build();

        Role memberRole = roleRepository.findByCode(Constants.DefaultRole.MEMBER.name()).orElse(null);

        if (Objects.nonNull(memberRole)) {
            UserRole userRole = UserRole.builder()
                    .id(IdUtils.nextId())
                    .userId(user.getId())
                    .roleId(memberRole.getId())
                    .deleted(Boolean.FALSE)
                    .build();

            userRoleRepository.save(userRole);
        }

        String activationOtp = RandomStringUtils.randomNumeric(6);

        emailService.sendActivationAccountEmail(user.getEmail(), user.getId(), user.getUsername(), activationOtp);

        cacheService.put(Constants.CacheName.USER_OTP_CACHE_NAME, user.getUsername(), activationOtp);

        userRepository.save(user);
    }

    @Override
    public void resendActivationCode(ResendActivationCodeRequest request) {
        User user = userRepository.findByCredential(request.getCredential())
                .orElseThrow(() -> new ResponseException(NotFoundError.USER_NOT_FOUND));

        if (Objects.equals(user.getStatus(), ActiveStatus.ACTIVE)) {
            throw new ResponseException(BadRequestError.USER_WAS_ACTIVATED);
        }

        String activationOtp = RandomStringUtils.randomNumeric(6);

        emailService.sendActivationAccountEmail(user.getEmail(), user.getId(), user.getUsername(), activationOtp);

        cacheService.put(Constants.CacheName.USER_OTP_CACHE_NAME, user.getUsername(), activationOtp);
    }

    @Override
    public void activeAccount(ActiveAccountRequest request) {
        User user = userRepository.findByCredential(request.getCredential())
                .orElseThrow(() -> new ResponseException(NotFoundError.USER_NOT_FOUND));

        if (Objects.equals(user.getStatus(), ActiveStatus.ACTIVE)) {
            throw new ResponseException(BadRequestError.USER_WAS_ACTIVATED);
        }

        user.setStatus(ActiveStatus.ACTIVE);

        userRepository.save(user);
    }

    @Override
    public void activeAccount(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseException(NotFoundError.USER_NOT_FOUND));

        if (Objects.equals(user.getStatus(), ActiveStatus.ACTIVE)) {
            throw new ResponseException(BadRequestError.USER_WAS_ACTIVATED);
        }

        user.setStatus(ActiveStatus.ACTIVE);

        userRepository.save(user);
    }

    @Override
    public AuthenticateResponse login(AuthenticateRequest request) {
        User user = userRepository.findByCredential(request.getCredential())
                .orElseThrow(() -> new ResponseException(NotFoundError.USER_NOT_FOUND));

        log.warn("User {} start login", user.getUsername());

        Authentication authentication = new CustomUserAuthentication(request.getCredential(), request.getPassword());

        authentication = authenticationManager.authenticate(authentication);

        String accessToken = tokenProvider.buildToken(authentication, user.getId(), TokenType.ACCESS_TOKEN);
        Duration accessTokenExpiresIn = authenticationProperties.getAccessTokenExpiresIn();
        Instant accessTokenExpiresAt = Instant.now().plus(accessTokenExpiresIn);

        String refreshToken;
        Duration refreshTokenExpiresIn;
        Instant refreshTokenExpiresAt;

        if (Objects.equals(request.getRememberMe(), Boolean.TRUE)) {
            refreshToken = tokenProvider.buildToken(authentication, user.getId(), TokenType.REFRESH_TOKEN);
            refreshTokenExpiresIn = authenticationProperties.getRefreshTokenExpiresIn();
        } else {
            refreshToken = tokenProvider.buildToken(authentication, user.getId(), TokenType.REFRESH_TOKEN_LONG);
            refreshTokenExpiresIn = authenticationProperties.getRefreshTokenLongExpiresIn();
        }

        refreshTokenExpiresAt = Instant.now().plus(refreshTokenExpiresIn);

        log.warn("User {} completed authentication", user.getUsername());

        return AuthenticateResponse.builder()
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.toSeconds())
                .accessTokenExpiredAt(accessTokenExpiresAt)
                .refreshToken(refreshToken)
                .refreshTokenExpiresIn(refreshTokenExpiresIn.toSeconds())
                .refreshTokenExpiredAt(refreshTokenExpiresAt)
                .build();
    }

    @Override
    public AuthenticateResponse refreshToken(RefreshTokenRequest request) {
        if (tokenCacheService.isRevokedRefreshToken(request.getRefreshToken(), request.getRememberMe())) {
            throw new ResponseException(AuthenticationError.TOKEN_WAS_REVOKED);
        }

        UUID userId = IdUtils.convertStringToUUID(tokenProvider.extractUserId(request.getRefreshToken()));
        Date issuedAt = tokenProvider.extractIssuedAt(request.getRefreshToken());
        Date expiration = tokenProvider.extractExpiration(request.getRefreshToken());

        if (Objects.isNull(userId) || Objects.isNull(issuedAt) || Objects.isNull(expiration) || expiration.before(Date.from(Instant.now()))) {
            throw new ResponseException(AuthenticationError.INVALID_AUTHENTICATION_TOKEN);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseException(NotFoundError.USER_NOT_FOUND));

        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), "", new ArrayList<>());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = tokenProvider.buildToken(authentication, userId, TokenType.ACCESS_TOKEN);
        Duration accessTokenExpiresIn = authenticationProperties.getAccessTokenExpiresIn();
        Instant accessTokenExpiresAt = Instant.now().plus(accessTokenExpiresIn);

        Duration refreshTokenExpiresIn;
        Instant refreshTokenExpiresAt;

        if (Objects.equals(request.getRememberMe(), Boolean.TRUE)) {
            refreshTokenExpiresIn = authenticationProperties.getRefreshTokenExpiresIn().minus(Duration.between(Instant.now(), issuedAt.toInstant()));
        } else {
            refreshTokenExpiresIn = authenticationProperties.getRefreshTokenLongExpiresIn().minus(Duration.between(Instant.now(), issuedAt.toInstant()));
        }

        refreshTokenExpiresAt = Instant.now().plus(refreshTokenExpiresIn);

        return AuthenticateResponse.builder()
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.toSeconds())
                .accessTokenExpiredAt(accessTokenExpiresAt)
                .refreshToken(request.getRefreshToken())
                .refreshTokenExpiresIn(refreshTokenExpiresIn.getSeconds())
                .refreshTokenExpiredAt(refreshTokenExpiresAt)
                .build();
    }

    @Override
    public ProfileResponse getAccountProfile() {
        UUID currentUserId = SecurityUtils.getCurrentUserId();

        UserAuthority userAuthority = authenticationService.getUserAuthority(currentUserId);
        UserInformation userInformation = userInformationRepository.findById(currentUserId).orElse(null);

        return autoMapper.toResponse(userAuthority, userInformation);
    }

    @Override
    public AuthenticateResponse loginGoogle(String code) throws IOException {

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUrl);
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        Map<String, Object> tokenData = restTemplate.exchange(
                tokenUrl, HttpMethod.POST, request, Map.class
        ).getBody();

        String accessToken = (String) tokenData.get("access_token");

        HttpHeaders headers1 = new HttpHeaders();

        headers1.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers1);

        Map<String, Object> userInformationData = restTemplate.exchange(
                userInforUrl, HttpMethod.GET, entity, Map.class
        ).getBody();

        Optional<User> existingUser = userRepository.findByEmail(userInformationData.get("email").toString());

        User user;

        if (existingUser.isPresent()) {
            user = existingUser.get();

            user.setUsername(userInformationData.get("name").toString());

        } else {
            user = User.builder()
                    .id(IdUtils.nextId())
                    .status(ActiveStatus.ACTIVE)
                    .email(userInformationData.get("email").toString())
                    .username(userInformationData.get("name").toString())
                    .accountType(AccountType.GOOGLE)
                    .deleted(Boolean.FALSE)
                    .password("google-auth-" + UUID.randomUUID())
                    .build();

            userRepository.save(user);
        }
        Optional<UserInformation> optionalUserInformation = userInformationRepository.findByUserId(user.getId());

        UserInformation userInformation;

        userInformation = optionalUserInformation.orElseGet(
                () -> UserInformation.builder()
                        .id(IdUtils.nextId())
                        .lastName(userInformationData.get("given_name").toString())
                        .firstName(null)
                        .middleName(null)
                        .address(null)
                        .avatarUrl(userInformationData.get("picture").toString())
                        .dateOfBirth(null)
                        .deleted(Boolean.FALSE)
                        .phone(null)
                        .gender(null)
                        .userId(user.getId())
                        .build());

        userInformationRepository.save(userInformation);

        Role memberRole = roleRepository.findByCode(Constants.DefaultRole.MEMBER.name()).orElse(null);

        if (Objects.nonNull(memberRole)) {

            UserRole userRole = UserRole.builder()
                    .id(IdUtils.nextId())
                    .userId(user.getId())
                    .roleId(memberRole.getId())
                    .deleted(Boolean.FALSE)
                    .build();

            userRoleRepository.save(userRole);

        }

        return AuthenticateResponse.builder()
                .accessToken(accessToken)
                .accessTokenExpiresIn(Long.parseLong(tokenData.get("expires_in").toString()))
                .accessTokenExpiredAt(Instant.now().plusSeconds(Long.parseLong(tokenData.get("expires_in").toString())))
    @Override
    public AccountBalanceResponse getAccountBalance() {
        UUID currentUserId = SecurityUtils.getCurrentUserId();

        BigDecimal balance = transactionRepository.findUserBalance(currentUserId);

        return AccountBalanceResponse.builder()
                .balance(balance)
                .build();
    }
}

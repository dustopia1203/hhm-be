package com.hhm.api.service;

import com.hhm.api.model.dto.UserAuthority;

import java.util.UUID;

public interface AuthenticationService {
    UserAuthority getUserAuthority(UUID id);
}

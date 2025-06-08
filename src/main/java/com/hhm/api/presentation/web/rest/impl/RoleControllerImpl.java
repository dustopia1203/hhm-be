package com.hhm.api.presentation.web.rest.impl;

import com.hhm.api.model.dto.response.Response;
import com.hhm.api.model.entity.Role;
import com.hhm.api.presentation.web.rest.RoleController;
import com.hhm.api.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoleControllerImpl implements RoleController {
    private final RoleService roleService;

    @Override
    public Response<List<Role>> getAvailableRoles() {
        return Response.of(roleService.getAvailableRoles());
    }
}

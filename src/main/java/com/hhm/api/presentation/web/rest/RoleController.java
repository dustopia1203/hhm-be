package com.hhm.api.presentation.web.rest;

import com.hhm.api.model.dto.response.Response;
import com.hhm.api.model.entity.Role;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Tag(name = "Role Resources")
@RequestMapping("/api/roles")
@Validated
public interface RoleController {
    @Operation(summary = "Get available roles")
    @GetMapping("/available")
    @PreAuthorize("hasPermission(null, 'ROLE:READ')")
    Response<List<Role>> getAvailableRoles();
}

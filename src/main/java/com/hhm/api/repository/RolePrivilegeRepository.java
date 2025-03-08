package com.hhm.api.repository;

import com.hhm.api.model.entity.RolePrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface RolePrivilegeRepository extends JpaRepository<RolePrivilege, UUID> {
    @Query("SELECT rp FROM RolePrivilege rp WHERE rp.deleted = FALSE AND rp.roleId IN :roleIds")
    List<RolePrivilege> findAllByRoleId(List<UUID> roleIds);
}

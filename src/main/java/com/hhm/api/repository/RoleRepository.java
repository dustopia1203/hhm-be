package com.hhm.api.repository;

import com.hhm.api.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    @Query("SELECT r FROM Role r WHERE r.deleted = FALSE AND r.status = 'ACTIVE' AND r.id = :id")
    Optional<Role> findActiveById(UUID id);

    @Query("SELECT r FROM Role r WHERE r.deleted = FALSE AND r.code = :code")
    Optional<Role> findByCode(String code);

    @Query("SELECT r FROM Role r WHERE r.deleted = FALSE AND r.code IN :codes")
    List<Role> findAllByCodes(List<String> codes);

    @Query("SELECT r FROM Role r WHERE r.deleted = FALSE AND r.status = 'ACTIVE' AND r.id IN :ids")
    List<Role> findActiveByIds(List<UUID> ids);

    @Query("SELECT r FROM Role r WHERE r.deleted = FALSE AND r.status = 'ACTIVE'")
    List<Role> findAllActive();
}

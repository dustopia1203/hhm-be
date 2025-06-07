package com.hhm.api.repository;

import com.hhm.api.model.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRoleRepository extends JpaRepository<UserRole, UUID> {
    @Query("SELECT ur FROM UserRole ur WHERE ur.deleted = FALSE and ur.userId = :userId")
    List<UserRole> findByUserId(UUID userId);

    @Query("SELECT COUNT(ur) > 0 FROM UserRole ur WHERE ur.deleted = FALSE and ur.userId = :userId and ur.roleId = :roleId")
    Boolean existsByUserIdAndRoleIdAndDeletedFalse(UUID userId,UUID roleId);

}

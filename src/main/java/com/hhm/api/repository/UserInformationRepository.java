package com.hhm.api.repository;

import com.hhm.api.model.entity.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserInformationRepository extends JpaRepository<UserInformation, UUID> {
    @Query("SELECT ui FROM UserInformation ui WHERE ui.deleted = FALSE AND ui.id = :id")
    @NonNull
    Optional<UserInformation> findById(@NonNull UUID id);

    @Query("SELECT ui FROM UserInformation ui WHERE ui.deleted = FALSE AND ui.id in :ids")
    List<UserInformation> findByIds(List<UUID> ids);

    @Query("SELECT ui FROM UserInformation ui WHERE ui.deleted = FALSE AND ui.userId in :userIds")
    List<UserInformation> findByUserIds(List<UUID> userIds);

    @Query("SELECT ui FROM UserInformation ui WHERE ui.deleted = FALSE AND ui.userId = :userId")
    Optional<UserInformation> findByUserId(UUID userId);
}

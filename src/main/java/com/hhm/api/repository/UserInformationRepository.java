package com.hhm.api.repository;

import com.hhm.api.model.entity.UserInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.UUID;

public interface UserInformationRepository extends JpaRepository<UserInformation, UUID> {
    @Query("SELECT ui FROM UserInformation ui WHERE ui.deleted = FALSE AND ui.id = :id")
    @NonNull
    Optional<UserInformation> findById(@NonNull UUID id);

    @Query("SELECT ui FROM UserInformation ui WHERE ui.deleted = FALSE AND ui.userId = :userId")
    @NonNull
    Optional<UserInformation> findByUserId(@NonNull UUID userId);
}

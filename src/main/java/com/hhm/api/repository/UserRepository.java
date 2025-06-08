package com.hhm.api.repository;

import com.hhm.api.model.entity.User;
import com.hhm.api.repository.custom.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID>, UserRepositoryCustom {
    @Query("SELECT u FROM User u WHERE u.deleted = FALSE AND u.id = :id")
    @NonNull
    Optional<User> findById(@NonNull UUID id);

    @Query("SELECT u FROM User u WHERE u.deleted = FALSE AND u.status = 'ACTIVE' AND u.id = :id")
    Optional<User> findActiveById(@NonNull UUID id);

    @Query("SELECT u FROM User u WHERE u.deleted = FALSE AND (u.username = :credential OR u.email = :credential)")
    Optional<User> findByCredential(String credential);

    @Query("SELECT u FROM User u WHERE u.deleted = FALSE AND u.accountType = 'SYSTEM' AND u.username = :username")
    Optional<User> findSystemByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.deleted = FALSE AND u.accountType = 'SYSTEM' AND u.email = :email")
    Optional<User> findSystemByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.deleted = FALSE AND u.username IN :usernames")
    List<User> findByUsernames(List<String> usernames);

    @Query("SELECT u FROM User u WHERE u.deleted = FALSE AND u.id IN :ids")
    List<User> findByIds(List<UUID> ids);

    @Query("SELECT u FROM User u WHERE u.deleted = FALSE AND u.email = :email")
    Optional<User> findByEmail(String email);
}

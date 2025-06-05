package com.hhm.api.repository;

import com.hhm.api.model.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    @Query("SELECT ci FROM CartItem ci WHERE ci.deleted = FALSE AND ci.id = :id")
    @NonNull
    Optional<CartItem> findById(@NonNull UUID id);

    @Query("SELECT ci FROM CartItem ci WHERE ci.deleted = FALSE AND ci.id in :ids")
    List<CartItem> findByIds(List<UUID> ids);

    @Query("SELECT ci FROM CartItem ci WHERE ci.deleted = FALSE AND ci.id in :ids AND ci.userId = :userId")
    List<CartItem> findByIdsAndUser(List<UUID> ids, UUID userId);

    @Query("SELECT ci FROM CartItem ci WHERE ci.deleted = FALSE AND ci.userId = :userId")
    List<CartItem> findByUser(UUID userId);

    @Query("SELECT ci FROM CartItem ci WHERE ci.deleted = FALSE AND ci.productId = :productId")
    Optional<CartItem> findByProduct(UUID productId);
}
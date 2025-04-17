package com.hhm.api.repository;

import com.hhm.api.model.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartItemRepository extends JpaRepository<CartItem, UUID> {
    @Query("SELECT ci FROM CartItem ci WHERE ci.deleted = FALSE AND ci.productId = :productId")
    Optional<CartItem> findByProduct(UUID productId);

    @Query("SELECT ci FROM CartItem ci WHERE ci.deleted = FALSE AND ci.cartId = :cartId")
    List<CartItem> findByCartId(UUID cartId);

    @Query("SELECT ci FROM CartItem ci WHERE ci.deleted = FALSE AND ci.id in :ids")
    List<CartItem> findByIds(List<UUID> ids);
}

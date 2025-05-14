package com.hhm.api.repository;

import com.hhm.api.model.entity.OrderItem;
import com.hhm.api.repository.custom.OrderItemRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID>, OrderItemRepositoryCustom {
    @Query("SELECT oi FROM OrderItem oi WHERE oi.deleted = FALSE AND oi.id = :id")
    @NonNull
    Optional<OrderItem> findById(@NonNull UUID id);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.deleted = FALSE AND oi.id = :id AND oi.shopId = :shopId")
    Optional<OrderItem> findByIdAndShop(UUID id, UUID shopId);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.deleted = FALSE AND oi.id = :id AND oi.userId = :userId")
    Optional<OrderItem> findByIdAndUser(UUID id, UUID userId);

    @Query("SELECT COUNT(oi) FROM OrderItem oi WHERE oi.deleted = FALSE AND oi.productId = :productId")
    Long countByProduct(UUID productId);
}

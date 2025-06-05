package com.hhm.api.repository;

import com.hhm.api.model.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RefundRepository extends JpaRepository<Refund, UUID> {
    @Query("SELECT r FROM Refund r WHERE r.deleted = FALSE AND r.id = :id")
    @NonNull
    Optional<Refund> findById(@NonNull UUID id);

    @Query("SELECT r FROM Refund r WHERE r.deleted = FALSE AND r.orderItemId = :orderItemId")
    Optional<Refund> findByOrderItem(UUID orderItemId);

    @Query("SELECT r FROM Refund r WHERE r.deleted = FALSE AND r.orderItemId in :orderItemIds")
    List<Refund> findAllByOrderItems(List<UUID> orderItemIds);
}

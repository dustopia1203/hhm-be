package com.hhm.api.repository;

import com.hhm.api.model.entity.Shop;
import com.hhm.api.repository.custom.ShopRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShopRepository extends JpaRepository<Shop, UUID>, ShopRepositoryCustom {
    @Query("SELECT s FROM Shop s WHERE s.deleted = FALSE AND s.id = :id")
    @NonNull
    Optional<Shop> findById(@NonNull UUID id);

    @Query("SELECT s FROM Shop s WHERE s.deleted = FALSE AND s.userId = :userId")
    Optional<Shop> findByUser(UUID userId);

    @Query("SELECT s FROM Shop s WHERE s.deleted = FALSE and s.status = 'ACTIVE' AND s.id = :id")
    Optional<Shop> findActiveById(UUID id);

    @Query("SELECT s FROM Shop s WHERE s.deleted = FALSE AND s.id in :ids")
    List<Shop> findByIds(List<UUID> ids);
}

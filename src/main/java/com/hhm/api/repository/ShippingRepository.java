package com.hhm.api.repository;

import com.hhm.api.model.entity.Shipping;
import com.hhm.api.repository.custom.ShippingRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ShippingRepository extends JpaRepository<Shipping, UUID>, ShippingRepositoryCustom {
    @Query("SELECT s FROM Shipping s WHERE s.deleted = FALSE and s.id = :id")
    @NonNull
    Optional<Shipping> findById(@NonNull UUID id);

    @Query("SELECT s FROM Shipping s WHERE s.deleted = FALSE and s.id IN :ids")
    List<Shipping> findByIds(List<UUID> ids);
}

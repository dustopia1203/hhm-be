package com.hhm.api.repository;

import com.hhm.api.model.entity.Product;
import com.hhm.api.repository.custom.ProductRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, ProductRepositoryCustom {
    @Query("SELECT p FROM Product p WHERE p.deleted = FALSE AND p.id = :id")
    @NonNull
    Optional<Product> findById(@NonNull UUID id);

    @Query("SELECT p FROM Product p WHERE p.deleted = FALSE AND p.id in :ids")
    List<Product> findByIds(List<UUID> ids);
}

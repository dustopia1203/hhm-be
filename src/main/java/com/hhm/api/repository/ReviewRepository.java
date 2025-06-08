package com.hhm.api.repository;

import com.hhm.api.model.entity.Review;
import com.hhm.api.model.entity.projection.ReviewStat;
import com.hhm.api.repository.custom.ReviewRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID>, ReviewRepositoryCustom {
    @Query("SELECT COUNT(r) as reviewCount, AVG(r.rating) as avgRating FROM Review r WHERE r.deleted = FALSE AND r.shopId = :shopId")
    ReviewStat findStatByShop(UUID shopId);

    @Query("SELECT COUNT(r) as reviewCount, AVG(r.rating) as avgRating FROM Review r WHERE r.deleted = FALSE AND r.productId = :productId")
    ReviewStat findStatByProduct(UUID productId);
}

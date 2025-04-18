package com.hhm.api.repository;

import com.hhm.api.model.entity.Review;
import com.hhm.api.model.entity.projection.ReviewStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
    @Query("SELECT COUNT(r), AVG(r.rating) FROM Review r WHERE r.deleted = FALSE AND r.shopId = :shopId")
    ReviewStat findStatByShop(UUID shopId);
}

package com.hhm.api.repository;

import com.hhm.api.model.entity.Category;
import com.hhm.api.repository.custom.CategoryRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID>, CategoryRepositoryCustom {
    @Query("SELECT c FROM Category c WHERE c.deleted = FALSE AND c.id = :id")
    @NonNull
    Optional<Category> findById(@NonNull UUID id);

    @Query("SELECT c FROM Category c WHERE c.deleted = FALSE AND c.status = 'ACTIVE' AND c.id = :id")
    Optional<Category> findActiveById(UUID id);

    @Query("SELECT c FROM Category c WHERE c.deleted = FALSE AND c.id in :ids")
    List<Category> findByIds(List<UUID> ids);

    @Query(
            value = """
                WITH RECURSIVE category_tree AS (
                    SELECT c1.* FROM category c1 
                        WHERE c1.deleted = FALSE AND id = :parentId
                    
                    UNION ALL
                    
                    SELECT c2.* FROM category c2
                        JOIN category_tree ct ON ct.id = c2.parent_id
                        WHERE c2.deleted = FALSE
                )    
            
                SELECT * FROM category_tree
            """,
            nativeQuery = true)
    List<Category> findTreeByParent(UUID parentId);
}

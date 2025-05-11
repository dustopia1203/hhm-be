package com.hhm.api.repository.custom.impl;

import com.hhm.api.model.dto.request.ReviewSearchRequest;
import com.hhm.api.model.entity.Review;
import com.hhm.api.repository.custom.ReviewRepositoryCustom;
import com.hhm.api.support.util.QueryUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long count(ReviewSearchRequest request) {
        Map<String, Object> values = new HashMap<>();

        String queryString = "SELECT COUNT(r) FROM Review r " + createCriteriaQuery(request, values);

        Query query = entityManager.createQuery(queryString, Long.class);

        values.forEach(query::setParameter);

        return (Long) query.getSingleResult();
    }

    @Override
    public List<Review> search(ReviewSearchRequest request) {
        Map<String, Object> values = new HashMap<>();

        String queryString = "SELECT r FROM Review r " + createCriteriaQuery(request, values) + QueryUtils.createOrderQuery(request, "r");

        Query query = entityManager.createQuery(queryString, Review.class);

        values.forEach(query::setParameter);

        query.setFirstResult((request.getPageIndex() - 1) * request.getPageSize());
        query.setMaxResults(request.getPageSize());

        return query.getResultList();
    }

    private String createCriteriaQuery(ReviewSearchRequest request, Map<String, Object> values) {
        StringBuilder criteriaQuery = new StringBuilder("WHERE 1 = 1 ");

        criteriaQuery.append("AND s.deleted = FALSE ");

        if (Objects.nonNull(request.getKeyword())) {
            criteriaQuery.append("AND (s.description LIKE :keyword) ");

            values.put("keyword", QueryUtils.encodeLikeString(request.getKeyword()));
        }

        if (!CollectionUtils.isEmpty(request.getUserIds())) {
            criteriaQuery.append("AND s.userId IN :userIds ");

            values.put("userIds", request.getUserIds());
        }

        if (!CollectionUtils.isEmpty(request.getShopIds())) {
            criteriaQuery.append("AND s.shopId IN :shopIds ");

            values.put("shopIds", request.getShopIds());
        }

        if (!CollectionUtils.isEmpty(request.getOrderItemIds())) {
            criteriaQuery.append("AND s.orderItemId IN :orderItemIds ");

            values.put("orderItemIds", request.getOrderItemIds());
        }

        if (!CollectionUtils.isEmpty(request.getProductIds())) {
            criteriaQuery.append("AND s.productId IN :productIds ");

            values.put("productIds", request.getProductIds());
        }

        if (Objects.nonNull(request.getRating())) {
            criteriaQuery.append("AND s.rating = :rating ");

            values.put("rating", request.getRating());
        }

        return criteriaQuery.toString();
    }
}

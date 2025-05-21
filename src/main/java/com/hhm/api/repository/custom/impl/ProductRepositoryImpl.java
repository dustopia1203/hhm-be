package com.hhm.api.repository.custom.impl;

import com.hhm.api.model.dto.request.ProductSearchRequest;
import com.hhm.api.model.entity.Product;
import com.hhm.api.repository.custom.ProductRepositoryCustom;
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
public class ProductRepositoryImpl implements ProductRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long count(ProductSearchRequest request) {
        Map<String, Object> values = new HashMap<>();

        String queryString = "SELECT COUNT(p) FROM Product p " + createCriteriaQuery(request, values);

        Query query = entityManager.createQuery(queryString, Long.class);

        values.forEach(query::setParameter);

        return (Long) query.getSingleResult();
    }

    @Override
    public List<Product> search(ProductSearchRequest request) {
        Map<String, Object> values = new HashMap<>();

        String queryString = "SELECT p FROM Product p " + createCriteriaQuery(request, values) + QueryUtils.createOrderQuery(request, "p");

        Query query = entityManager.createQuery(queryString, Product.class);

        values.forEach(query::setParameter);

        query.setFirstResult((request.getPageIndex() - 1) * request.getPageSize());
        query.setMaxResults(request.getPageSize());

        return query.getResultList();
    }

    private String createCriteriaQuery(ProductSearchRequest request, Map<String, Object> values) {
        StringBuilder criteriaQuery = new StringBuilder("WHERE 1 = 1 ");

        criteriaQuery.append("AND p.deleted = FALSE ");

        if (Objects.nonNull(request.getKeyword())) {
            criteriaQuery.append("AND (p.name LIKE :keyword OR p.description LIKE :keyword) ");

            values.put("keyword", QueryUtils.encodeLikeString(request.getKeyword()));
        }

        if (!CollectionUtils.isEmpty(request.getIds())) {
            criteriaQuery.append("AND p.id IN :ids ");

            values.put("ids", request.getIds());
        }

        if (!CollectionUtils.isEmpty(request.getShopIds())) {
            criteriaQuery.append("AND p.shopId IN :shopIds ");

            values.put("shopIds", request.getShopIds());
        }

        if (!CollectionUtils.isEmpty(request.getCategoryIds())) {
            criteriaQuery.append("AND (p.categoryId IN :categoryIds OR p.categoryId IN (SELECT c.id FROM Category c WHERE c.parentId IN :categoryIds)) ");

            values.put("categoryIds", request.getCategoryIds());
        }

        if (Objects.nonNull(request.getStatus())) {
            criteriaQuery.append("AND p.status = :status ");

            values.put("status", request.getStatus());
        }

        if (Objects.nonNull(request.getMinPrice())) {
            criteriaQuery.append("AND p.price >= :minPrice ");

            values.put("minPrice", request.getMinPrice());
        }

        if (Objects.nonNull(request.getMaxPrice())) {
            criteriaQuery.append("AND p.price <= :maxPrice ");

            values.put("maxPrice", request.getMaxPrice());
        }

        return criteriaQuery.toString();
    }
}

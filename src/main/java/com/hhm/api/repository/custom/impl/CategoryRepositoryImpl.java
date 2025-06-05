package com.hhm.api.repository.custom.impl;

import com.hhm.api.model.dto.request.CategorySearchRequest;
import com.hhm.api.model.entity.Category;
import com.hhm.api.repository.custom.CategoryRepositoryCustom;
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
public class CategoryRepositoryImpl implements CategoryRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long count(CategorySearchRequest request) {
        Map<String, Object> values = new HashMap<>();

        String queryString = "SELECT COUNT(c) FROM Category c " + createCriteriaQuery(request, values);

        Query query = entityManager.createQuery(queryString, Long.class);

        values.forEach(query::setParameter);

        return (Long) query.getSingleResult();
    }

    @Override
    public List<Category> search(CategorySearchRequest request) {
        Map<String, Object> values = new HashMap<>();

        String queryString = "SELECT c FROM Category c " + createCriteriaQuery(request, values) + QueryUtils.createOrderQuery(request, "c");

        Query query = entityManager.createQuery(queryString, Category.class);

        values.forEach(query::setParameter);

        query.setFirstResult((request.getPageIndex() - 1) * request.getPageSize());
        query.setMaxResults(request.getPageSize());

        return query.getResultList();
    }

    private String createCriteriaQuery(CategorySearchRequest request, Map<String, Object> values) {
        StringBuilder criteriaQuery = new StringBuilder("WHERE 1 = 1 ");

        criteriaQuery.append("AND c.deleted = FALSE ");

        if (Objects.nonNull(request.getKeyword())) {
            criteriaQuery.append("AND c.name LIKE :keyword ");

            values.put("keyword", QueryUtils.encodeLikeString(request.getKeyword()));
        }

        if (!CollectionUtils.isEmpty(request.getIds())) {
            criteriaQuery.append("AND c.id IN :ids ");

            values.put("ids", request.getIds());
        }

        if (!CollectionUtils.isEmpty(request.getParentIds())) {
            criteriaQuery.append("AND c.parentId IN :parentIds ");

            values.put("parentIds", request.getParentIds());
        } else {
            criteriaQuery.append("AND c.parentId IS NULL ");
        }

        if (Objects.nonNull(request.getStatus())) {
            criteriaQuery.append("AND c.status = :status ");

            values.put("status", request.getStatus());
        }

        return criteriaQuery.toString();
    }
}

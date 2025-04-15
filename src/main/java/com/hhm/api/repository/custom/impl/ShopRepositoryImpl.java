package com.hhm.api.repository.custom.impl;

import com.hhm.api.model.dto.request.ShopSearchRequest;
import com.hhm.api.model.entity.Shop;
import com.hhm.api.repository.custom.ShopRepositoryCustom;
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
public class ShopRepositoryImpl implements ShopRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long count(ShopSearchRequest request) {
        Map<String, Object> values = new HashMap<>();

        String queryString = "SELECT COUNT(s) FROM Shop s " + createCriteriaQuery(request, values);

        Query query = entityManager.createQuery(queryString, Long.class);

        values.forEach(query::setParameter);

        return (Long) query.getSingleResult();
    }

    @Override
    public List<Shop> search(ShopSearchRequest request) {
        Map<String, Object> values = new HashMap<>();

        String queryString = "SELECT s FROM Shop s " + createCriteriaQuery(request, values) + QueryUtils.createOrderQuery(request, "s");

        Query query = entityManager.createQuery(queryString, Shop.class);

        values.forEach(query::setParameter);

        return query.getResultList();
    }

    private String createCriteriaQuery(ShopSearchRequest request, Map<String, Object> values) {
        StringBuilder criteriaQuery = new StringBuilder("WHERE 1 = 1 ");

        criteriaQuery.append("AND s.deleted = FALSE ");

        if (!Objects.isNull(request.getKeyword())) {
            criteriaQuery.append("AND (s.name LIKE :keyword OR s.address LIKE :keyword) ");

            values.put("keyword", QueryUtils.encodeLikeString(request.getKeyword()));
        }

        if (!CollectionUtils.isEmpty(request.getUserIds())) {
            criteriaQuery.append("AND s.userId IN :userIds ");

            values.put("userIds", request.getUserIds());
        }

        if (Objects.nonNull(request.getStatus())) {
            criteriaQuery.append("AND s.status = :status ");

            values.put("status", request.getStatus());
        }

        return criteriaQuery.toString();
    }
}

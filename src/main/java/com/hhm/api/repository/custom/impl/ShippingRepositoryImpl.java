package com.hhm.api.repository.custom.impl;

import com.hhm.api.model.dto.request.ShippingSearchRequest;
import com.hhm.api.model.entity.Shipping;
import com.hhm.api.repository.custom.ShippingRepositoryCustom;
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
public class ShippingRepositoryImpl implements ShippingRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long count(ShippingSearchRequest request) {
        Map<String, Object> values = new HashMap<>();

        String queryString = "SELECT COUNT(s) FROM Shipping s " + createCriteriaQuery(request, values);

        Query query = entityManager.createQuery(queryString, Long.class);

        values.forEach(query::setParameter);

        return (Long) query.getSingleResult();
    }

    @Override
    public List<Shipping> search(ShippingSearchRequest request) {
        Map<String, Object> values = new HashMap<>();

        String queryString = "SELECT s FROM Shipping s " + createCriteriaQuery(request, values) + QueryUtils.createOrderQuery(request, "s");

        Query query = entityManager.createQuery(queryString, Shipping.class);

        values.forEach(query::setParameter);

        query.setFirstResult((request.getPageIndex() - 1) * request.getPageSize());
        query.setMaxResults(request.getPageSize());

        return query.getResultList();
    }

    private String createCriteriaQuery(ShippingSearchRequest request, Map<String, Object> values) {
        StringBuilder criteriaQuery = new StringBuilder("WHERE 1 = 1 ");

        criteriaQuery.append("AND s.deleted = FALSE ");

        if (Objects.nonNull(request.getKeyword())) {
            criteriaQuery.append("AND (s.name LIKE :keyword) ");

            values.put("keyword", QueryUtils.encodeLikeString(request.getKeyword()));
        }

        if (!CollectionUtils.isEmpty(request.getIds())) {
            criteriaQuery.append("AND s.id IN :ids ");

            values.put("ids", request.getIds());
        }

        if (Objects.nonNull(request.getStatus())) {
            criteriaQuery.append("AND s.status = :status ");

            values.put("status", request.getStatus());
        }

        return criteriaQuery.toString();
    }
}

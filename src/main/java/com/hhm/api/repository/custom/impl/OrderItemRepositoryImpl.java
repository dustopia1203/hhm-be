package com.hhm.api.repository.custom.impl;

import com.hhm.api.model.dto.request.OrderItemSearchRequest;
import com.hhm.api.model.entity.OrderItem;
import com.hhm.api.repository.custom.OrderItemRepositoryCustom;
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
public class OrderItemRepositoryImpl implements OrderItemRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long count(OrderItemSearchRequest request) {
        Map<String, Object> values = new HashMap<>();

        String queryString = "SELECT COUNT(oi) FROM OrderItem oi " + createCriteriaQuery(request, values);

        Query query = entityManager.createQuery(queryString, Long.class);

        values.forEach(query::setParameter);

        return (Long) query.getSingleResult();
    }

    @Override
    public List<OrderItem> search(OrderItemSearchRequest request) {
        Map<String, Object> values = new HashMap<>();

        String queryString = "SELECT oi FROM OrderItem oi " + createCriteriaQuery(request, values) + QueryUtils.createOrderQuery(request, "oi");

        Query query = entityManager.createQuery(queryString, OrderItem.class);

        values.forEach(query::setParameter);

        query.setFirstResult((request.getPageIndex() - 1) * request.getPageSize());
        query.setMaxResults(request.getPageSize());

        return query.getResultList();
    }

    private String createCriteriaQuery(OrderItemSearchRequest request, Map<String, Object> values) {
        StringBuilder criteriaQuery = new StringBuilder("WHERE 1 = 1 ");

        criteriaQuery.append("AND oi.deleted = FALSE ");

        if (Objects.nonNull(request.getKeyword())) {
            criteriaQuery.append("AND (oi.address LIKE :keyword) ");

            values.put("keyword", QueryUtils.encodeLikeString(request.getKeyword()));
        }

        if (!CollectionUtils.isEmpty(request.getIds())) {
            criteriaQuery.append("AND oi.id IN :ids ");

            values.put("ids", request.getIds());
        }

        if (!CollectionUtils.isEmpty(request.getUserIds())) {
            criteriaQuery.append("AND oi.userId IN :userIds ");

            values.put("userIds", request.getUserIds());
        }

        if (!CollectionUtils.isEmpty(request.getShopIds())) {
            criteriaQuery.append("AND oi.shopId IN :shopIds ");

            values.put("shopIds", request.getShopIds());
        }

        if (!CollectionUtils.isEmpty(request.getShippingIds())) {
            criteriaQuery.append("AND oi.shippingId IN :shippingIds ");

            values.put("shippingIds", request.getShippingIds());
        }

        if (Objects.nonNull(request.getOrderItemStatus())) {
            criteriaQuery.append("AND oi.orderItemStatus = :orderItemStatus ");

            values.put("orderItemStatus", request.getOrderItemStatus());
        }

        return criteriaQuery.toString();
    }
}

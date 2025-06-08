package com.hhm.api.repository.custom.impl;

import com.hhm.api.model.dto.request.TransactionSearchRequest;
import com.hhm.api.model.entity.Transaction;
import com.hhm.api.repository.custom.TransactionRepositoryCustom;
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
public class TransactionRepositoryImpl implements TransactionRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long count(TransactionSearchRequest request) {
        Map<String, Object> values = new HashMap<>();

        String queryString = "SELECT COUNT(t) FROM Transaction t " + createCriteriaQuery(request, values);

        Query query = entityManager.createQuery(queryString, Long.class);

        values.forEach(query::setParameter);

        return (Long) query.getSingleResult();
    }

    @Override
    public List<Transaction> search(TransactionSearchRequest request) {
        Map<String, Object> values = new HashMap<>();

        String queryString = "SELECT t FROM Transaction t " + createCriteriaQuery(request, values) + QueryUtils.createOrderQuery(request, "t");

        Query query = entityManager.createQuery(queryString, Transaction.class);

        values.forEach(query::setParameter);

        query.setFirstResult((request.getPageIndex() - 1) * request.getPageSize());
        query.setMaxResults(request.getPageSize());

        return query.getResultList();
    }

    private String createCriteriaQuery(TransactionSearchRequest request, Map<String, Object> values) {
        StringBuilder criteriaQuery = new StringBuilder("WHERE 1 = 1 ");

        criteriaQuery.append("AND t.deleted = FALSE ");

        if (Objects.nonNull(request.getKeyword())) {
            criteriaQuery.append("AND (t.referenceContext LIKE :keyword) ");

            values.put("keyword", QueryUtils.encodeLikeString(request.getKeyword()));
        }

        if (!CollectionUtils.isEmpty(request.getIds())) {
            criteriaQuery.append("AND t.id IN :ids ");

            values.put("ids", request.getIds());
        }

        if (!CollectionUtils.isEmpty(request.getUserIds())) {
            criteriaQuery.append("AND t.userId IN :userIds ");

            values.put("userIds", request.getUserIds());
        }

        if (!CollectionUtils.isEmpty(request.getPaymentMethods())) {
            criteriaQuery.append("AND t.paymentMethod IN :paymentMethods ");

            values.put("paymentMethods", request.getPaymentMethods());
        }

        if (!CollectionUtils.isEmpty(request.getTransactionStatuses())) {
            criteriaQuery.append("AND t.transactionStatus IN :transactionStatuses ");

            values.put("transactionStatuses", request.getTransactionStatuses());
        }

        if (!CollectionUtils.isEmpty(request.getTransactionTypes())) {
            criteriaQuery.append("AND t.transactionType IN :transactionTypes ");

            values.put("transactionTypes", request.getTransactionTypes());
        }

        return criteriaQuery.toString();
    }
}

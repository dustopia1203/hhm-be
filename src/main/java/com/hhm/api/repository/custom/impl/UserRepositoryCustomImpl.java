package com.hhm.api.repository.custom.impl;

import com.hhm.api.model.dto.request.UserSearchRequest;
import com.hhm.api.model.entity.User;
import com.hhm.api.repository.custom.UserRepositoryCustom;
import com.hhm.api.support.util.QueryUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Long count(UserSearchRequest request) {
        Map<String, Object> values = new HashMap<>();

        String queryString = "SELECT COUNT(u) FROM User u " + createCriteriaQuery(request, values);

        Query query = entityManager.createQuery(queryString, Long.class);

        values.forEach(query::setParameter);

        return (Long) query.getSingleResult();
    }

    @Override
    public List<User> search(UserSearchRequest request) {
        Map<String, Object> values = new HashMap<>();

        String queryString = "SELECT u FROM User u" + createCriteriaQuery(request, values);

        Query query = entityManager.createQuery(queryString, User.class);

        values.forEach(query::setParameter);

        return query.getResultList();
    }

    private String createCriteriaQuery(UserSearchRequest request, Map<String, Object> values) {
        StringBuilder criteriaQuery = new StringBuilder(" WHERE 1=1 ");

        criteriaQuery.append(" AND u.deleted = FALSE ");

        if (!Objects.isNull(request.getKeyword())) {

            criteriaQuery.append(" AND u.username LIKE :keyword OR u.email LIKE :keyword OR u.phone LIKE :keyword");

            values.put("keyword", QueryUtils.encodeLikeString(request.getKeyword()));
        }

        if (Objects.nonNull(request.getStatus())) {

            criteriaQuery.append(" AND u.status = :status");

            values.put("status", request.getStatus());
        }

        if (Objects.nonNull(request.getAccountType())) {

            criteriaQuery.append(" AND u.accountType= :accountType");

            values.put("accountType", request.getAccountType());
        }

        return criteriaQuery.toString();
    }
}

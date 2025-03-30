package com.hhm.api.support.util;

import com.hhm.api.model.dto.request.PagingRequest;

import java.util.Objects;

public class QueryUtils {
    public static String encodeLikeString(String query) {
        return "%" + query + "%";
    }

    public static String createOrderQuery(PagingRequest request, String alias) {
        StringBuilder orderQuery = new StringBuilder();

        if (Objects.nonNull(request.getSortBy())) {
            orderQuery.append("ORDER BY ").append(alias).append(".").append(request.getSortBy());
        } else {
            orderQuery.append("ORDER BY ").append(alias).append(".createdAt DESC");
        }

        return orderQuery.toString();
    }
}

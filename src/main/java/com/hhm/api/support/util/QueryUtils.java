package com.hhm.api.support.util;

public class QueryUtils {
    public static String encodeLikeString(String query) {
        return "%" + query + "%";
    }
}

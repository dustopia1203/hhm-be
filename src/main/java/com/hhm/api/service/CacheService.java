package com.hhm.api.service;

public interface CacheService<K, V> {
    V get(String cacheName, K key);

    void put(String cacheName, K key, V value);

    void evict(String cacheName, K key);

    void clear(String cacheName);
}

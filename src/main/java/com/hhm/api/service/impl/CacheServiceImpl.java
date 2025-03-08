package com.hhm.api.service.impl;

import com.hhm.api.service.CacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CacheServiceImpl<K, V> implements CacheService<K, V> {
    private final RedisCacheManager cacheManager;

    @Override
    public V get(String cacheName, K key) {
        Cache cache = cacheManager.getCache(cacheName);

        return (V) (Objects.isNull(cache) ? null : cache.get(key));
    }

    @Override
    public void put(String cacheName, K key, V value) {
        Cache cache = cacheManager.getCache(cacheName);

        if (Objects.nonNull(cache)) {
            cache.put(key, value);
        }
    }

    @Override
    public void evict(String cacheName, K key) {
        Cache cache = cacheManager.getCache(cacheName);

        if (Objects.nonNull(cache)) {
            cache.evict(key);
        }
    }

    @Override
    public void clear(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);

        if (Objects.nonNull(cache)) {
            cache.clear();
        }
    }
}

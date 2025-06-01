package com.hhm.api.config;

import com.hhm.api.config.properties.CustomCacheProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Configuration
@EnableCaching
@AutoConfigureAfter({RedisAutoConfiguration.class})
@RequiredArgsConstructor
public class RedisConfiguration {
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory, CustomCacheProperties customCacheProperties) {
        RedisCacheConfiguration defaultCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig();
        defaultCacheConfiguration.disableCachingNullValues();

        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(defaultCacheConfiguration);

        Map<String, RedisCacheConfiguration> customCacheConfigurations = new HashMap<>();

        Optional.ofNullable(customCacheProperties)
                .map(CustomCacheProperties::getCustomCache)
                .ifPresent(
                        customCacheProperty ->
                                customCacheProperty.forEach(
                                        (key, value) -> {
                                            RedisCacheConfiguration customCacheConfiguration = handleCustomCacheConfiguration(value, defaultCacheConfiguration);

                                            customCacheConfigurations.put(key, customCacheConfiguration);
                                        }
                                )
                );

        builder.withInitialCacheConfigurations(customCacheConfigurations);

        return builder.build();
    }

    private RedisCacheConfiguration handleCustomCacheConfiguration(CacheProperties.Redis cacheProperty, RedisCacheConfiguration cacheConfiguration) {
        if (Objects.nonNull(cacheProperty)) {
            if (Objects.nonNull(cacheProperty.getTimeToLive())) {
                cacheConfiguration = cacheConfiguration.entryTtl(cacheProperty.getTimeToLive());
            }

            if (!Objects.equals(cacheProperty.isCacheNullValues(), Boolean.TRUE)) {
                cacheConfiguration = cacheConfiguration.disableCachingNullValues();
            }

            if (Objects.nonNull(cacheProperty.getKeyPrefix())) {
                cacheConfiguration = cacheConfiguration.computePrefixWith(cacheName -> cacheName + "_" + cacheProperty.getKeyPrefix());
            }

            if (Objects.equals(cacheProperty.isUseKeyPrefix(), Boolean.FALSE)) {
                cacheConfiguration = cacheConfiguration.disableKeyPrefix();
            }
        }

        return cacheConfiguration;
    }
}

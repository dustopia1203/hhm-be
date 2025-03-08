package com.hhm.api.config.properties;

import lombok.Data;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "spring.cache")
@Data
public class CustomCacheProperties {
    private Map<String, CacheProperties.Redis> customCache;
}

package com.hhm.api.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@ConfigurationProperties(prefix = "vnpay")
@Data
public class VNPayProperties {
    private String version;
    private String tmnCode;
    private String hashSecret;
    private String url;
    private String returnUrl;
    private Duration expiredTime;
}

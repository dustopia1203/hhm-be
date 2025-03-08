package com.hhm.api.config.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class ApplicationConfiguration {
    @Bean
    public AuditorAware<String> auditorAware() {
        return new CustomAuditorAware();
    }
}

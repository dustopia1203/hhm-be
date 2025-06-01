package com.hhm.api.config.application;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.List;
import java.util.Locale;

@Configuration
@EnableAsync
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableScheduling
public class ApplicationConfiguration {
    @Bean
    public AuditorAware<String> auditorAware() {
        return new CustomAuditorAware();
    }

    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();
        localeResolver.setDefaultLocale(new Locale("vi"));
        localeResolver.setSupportedLocales(List.of(new Locale("vi"), new Locale("en")));
        return localeResolver;
    }
}

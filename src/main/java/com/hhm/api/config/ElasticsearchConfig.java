package com.hhm.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.elasticsearch.config.ElasticsearchConfigurationSupport;
import org.springframework.data.elasticsearch.core.convert.ElasticsearchCustomConversions;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class ElasticsearchConfig extends ElasticsearchConfigurationSupport {

    @WritingConverter
    public static class InstantToStringConverter implements Converter<Instant, String> {
        @Override
        public String convert(Instant source) {
            return source.toString();
        }
    }

    @ReadingConverter
    public static class StringToInstantConverter implements Converter<String, Instant> {
        @Override
        public Instant convert(String source) {
            return Instant.parse(source);
        }
    }

    @Override
    @Bean
    public ElasticsearchCustomConversions elasticsearchCustomConversions() {
        List<Converter<?, ?>> converters = new ArrayList<>();
        converters.add(new InstantToStringConverter());
        converters.add(new StringToInstantConverter());
        return new ElasticsearchCustomConversions(converters);
    }
} 
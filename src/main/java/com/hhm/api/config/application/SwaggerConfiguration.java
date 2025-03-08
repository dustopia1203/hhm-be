package com.hhm.api.config.application;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "HHMShop API Gateway",
                description = "HHMShop API Gateway provides RESTful APIs for HHMShop Ecommerce Platform",
                termsOfService = "Term of service",
                contact = @Contact(
                        name = "HHMShop",
                        url = "https://hhmshop.com.vn",
                        email = "hhmshop.com"
                ),
                license = @License(
                        name = "The MIT License",
                        url = "https://opensource.org/license/mit"
                ),
                version = "0.0.1"
        ),
        servers = {
                @Server(
                        url = "http://localhost:8080",
                        description = "Local Develop URL"
                )
        },
        security = @SecurityRequirement(
            name = "JWT Bearer Authentication"
        )
)
@SecurityScheme(
        type = SecuritySchemeType.HTTP,
        name = "JWT Bearer Authentication",
        description = "JWT Access Token Bearer Authentication",
        in = SecuritySchemeIn.HEADER,
        scheme = "bearer",
        bearerFormat = "JWT"
)
@ConditionalOnExpression(value = "${springdoc.api-docs.enabled:false}")
public class SwaggerConfiguration {

    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
                .group("ALL")
                .pathsToMatch("/api/**")
                .build();
    }
}

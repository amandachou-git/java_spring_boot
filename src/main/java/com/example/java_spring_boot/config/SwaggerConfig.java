package com.example.java_spring_boot.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Java Spring Boot REST API")
                .version("Ver. 1.0.0")
                .description("List of Java Spring Boot REST APIs");

        String securitySchemaName = "JWT Authentication";
        SecurityRequirement securityRequirement =
                new SecurityRequirement().addList(securitySchemaName);
        Components components = new Components()
                .addSecuritySchemes(securitySchemaName,
                        new SecurityScheme()
                                .name(securitySchemaName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                );

        return new OpenAPI()
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}

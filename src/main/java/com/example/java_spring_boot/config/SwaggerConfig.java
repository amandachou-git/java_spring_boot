package com.example.java_spring_boot.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Java Spring Boot REST API")
                .description("Spring Boot REST API for Java")
                .version("1.0.0")
                .description("List of Java Spring Boot REST APIs");
        return new OpenAPI().info(info);
    }
}

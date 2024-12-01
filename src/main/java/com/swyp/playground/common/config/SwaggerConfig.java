package com.swyp.playground.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        SecurityScheme securityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .name("Authorization")
                .in(SecurityScheme.In.HEADER);

        Components components = new Components()
                .addSecuritySchemes("bearerAuth", securityScheme);

        // Define Security Requirement
        SecurityRequirement securityRequirement = new SecurityRequirement().addList("bearerAuth");

        Info info = new Info()
                .title("Playground Swagger")
                .description("Playground API Doc");


        return new OpenAPI()
                .components(components)
                .addSecurityItem(securityRequirement)
                .info(info);
    }
}
package com.example.contactmanager.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Contact Manager Service API",
                version = "1.0",
                description = "REST API for contact management"
        )
)
@Configuration
public class OpenApiConfig {
}

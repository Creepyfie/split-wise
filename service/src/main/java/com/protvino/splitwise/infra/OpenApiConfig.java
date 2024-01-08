package com.protvino.splitwise.infra;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info().title("Split wise OpenAPI Swagger"));
    }

    @Bean
    public GroupedOpenApi reportsApi() {
        return GroupedOpenApi.builder()
            .displayName("Split wise API")
            .group("controller")
            .packagesToScan("com.protvino.splitwise.port")
            .build();
    }
}

package com.vivy.springdoc;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenAPIAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OpenAPI openAPI() {
        return new BearerOpenAPI();
    }

}

package com.vivy.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import java.util.List;

@Configuration
@PropertySource("classpath:openapi.properties")
@AutoConfigureBefore(SwaggerUiConfigParameters.class)
public class OpenAPIAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public OpenAPI bearerOpenAPI() {
        return new OpenAPI().components(
                new Components().addSecuritySchemes(
                        "bearer-jwt",
                        new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")
                ))
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt", List.of("read", "write")));
    }
}

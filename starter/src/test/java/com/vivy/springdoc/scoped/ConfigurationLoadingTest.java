package com.vivy.springdoc.scoped;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = TestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class ConfigurationLoadingTest {

    @Autowired
    public OpenAPI openAPI;

    @Test
    public void isConfigurationLoadable() {
        SecurityScheme securityScheme = openAPI.getComponents().getSecuritySchemes().get("bearer-jwt");
        assertThat(securityScheme.getName()).isEqualTo(HttpHeaders.AUTHORIZATION);
        assertThat(securityScheme.getIn()).isEqualTo(SecurityScheme.In.HEADER);
        assertThat(securityScheme.getBearerFormat()).isEqualTo("JWT");
    }

}

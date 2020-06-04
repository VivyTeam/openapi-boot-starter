package com.vivy.springdoc.scoped;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;


public class ConfigurationLoadingTest extends AbstractIntegrationTest {

    @Autowired
    private OpenAPI openAPI;

    @Test
    public void isConfigurationLoadable() {
        SecurityScheme securityScheme = openAPI.getComponents().getSecuritySchemes().get("bearer-jwt");
        assertThat(securityScheme.getName()).isEqualTo(HttpHeaders.AUTHORIZATION);
        assertThat(securityScheme.getIn()).isEqualTo(SecurityScheme.In.HEADER);
        assertThat(securityScheme.getBearerFormat()).isEqualTo("JWT");
    }
}

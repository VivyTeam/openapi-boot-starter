package com.vivy.springdoc.scoped.startertest;

import com.vivy.support.AbstractIntegrationTest;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class StarterApiTests extends AbstractIntegrationTest {

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    private OpenAPI openAPI;

    @Test
    public void configurationIsLoadable() {
        var securityScheme = openAPI.getComponents().getSecuritySchemes().get("bearer-jwt");
        assertThat(securityScheme.getName()).isEqualTo(HttpHeaders.AUTHORIZATION);
        assertThat(securityScheme.getIn()).isEqualTo(SecurityScheme.In.HEADER);
        assertThat(securityScheme.getBearerFormat()).isEqualTo("JWT");
    }

    @Test
    public void doesExposeApiDefinition() {
        var apiDocs = restTemplate.getForEntity("/v3/api-docs", String.class);
        assertThat(apiDocs.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void doesNotExposeSwaggerUI() {
        var swaggerUi = restTemplate.getForEntity("/swagger-ui.html", String.class);
        assertThat(swaggerUi.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void apiDocIsGenerated() {
        var apiDocs = restTemplate.getForEntity("/v3/api-docs", String.class);
        assertThat(apiDocs.getBody()).contains("paths\":{\"/test\":{\"get");
    }

}

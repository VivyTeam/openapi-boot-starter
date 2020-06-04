package com.vivy.springdoc.scoped;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class OpenApiTest extends AbstractIntegrationTest {

    @Test
    public void doesExposeApiDefinition() {
        ResponseEntity<String> swagger = restTemplate.getForEntity("/v3/api-docs", String.class);
        assertThat(swagger.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void doesNotExposeSwaggerUI() {
        ResponseEntity<String> swagger = restTemplate.getForEntity("/swagger-ui.html", String.class);
        assertThat(swagger.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}

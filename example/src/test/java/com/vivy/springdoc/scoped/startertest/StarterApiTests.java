package com.vivy.springdoc.scoped.startertest;

import com.vivy.support.AbstractIntegrationTest;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

class StarterApiTests extends AbstractIntegrationTest {

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    private OpenAPI openAPI;

    @Autowired
    private Environment environment;

    @LocalServerPort
    private int localServerPort;

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

    @Test
    public void testThatApiDocFileAtTheLocationSpecifiedByEnvVariable() {
        //TODO BA-433, change back to the commented code when lombok issues are resolved
        //String apiDocFile = environment.getProperty("openapi.service.output");
        String apiDocFile = "build/api-docs.json";
        Path docApiFile = Paths.get(FileSystems.getDefault().getPath("").toAbsolutePath().toString(), apiDocFile);

        assertThat(docApiFile).exists();
    }

    @Test
    public void testThatApiDocFileContainsTheTheCorrectContent() throws IOException {
        //TODO BA-433, change back to the commented code when lombok issues are resolved
        //String apiDocFile = environment.getProperty("openapi.service.output");
        String apiDocFile = "build/api-docs.json";
        Path docApiFile = Paths.get(FileSystems.getDefault().getPath("").toAbsolutePath().toString(), apiDocFile);

        String openApiSpec = restTemplate.getForEntity("/v3/api-docs", String.class).getBody();
        assertThat(Files.readString(docApiFile)).isEqualTo(openApiSpec);
    }
}

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
    public void testThatApiDocFileIsCreatedByNameAndLocationSpecifiedInPropertiesFiles() {
        String apiDocFileName = environment.getProperty("openapi-boot-starter.swagger-ui.api-docs.file.name");
        String apiDocFilePath = environment.getProperty("openapi-boot-starter.swagger-ui.api-docs.file.path");
        Path docApiFile = Paths.get(FileSystems.getDefault().getPath("").toAbsolutePath().toString(), apiDocFilePath, apiDocFileName);

        assertThat(docApiFile).exists();
    }

    @Test
    public void testThatTheContentOfTheApiDocCreatedFileContainsBaseURlSpecifiedInThePropertiesFiles() throws IOException {
        String apiDocFileName = environment.getProperty("openapi-boot-starter.swagger-ui.api-docs.file.name");
        String apiDocFilePath = environment.getProperty("openapi-boot-starter.swagger-ui.api-docs.file.path");
        String host = environment.getProperty("openapi-boot-starter.swagger-ui.api-docs.url.host");
        String servicePort = environment.getProperty("openapi-boot-starter.swagger-ui.api-docs.url.port");
        Path docApiFile = Paths.get(FileSystems.getDefault().getPath("").toAbsolutePath().toString(), apiDocFilePath, apiDocFileName);
        String servicePortUrlSegment = servicePort.isBlank() ? servicePort : ":" + servicePort;
        String openApiSpec = restTemplate.getForEntity("/v3/api-docs", String.class).getBody().replace("http://localhost:" + localServerPort, "http://" + host + servicePortUrlSegment);
        String docApiSpec = Files.readString(docApiFile);

        assertThat(docApiSpec).isEqualTo(openApiSpec);
    }
}

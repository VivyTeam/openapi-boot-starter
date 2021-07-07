package com.vivy.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    @Bean
    @ConditionalOnBean(OpenAPI.class)
    public ApplicationRunner applicationRunner(Environment environment) {

        String APPLICATION_PROPERTIES_PREFIX = "openapi-boot-starter.swagger-ui.api-docs.";
        DefaultUriBuilderFactory uriFactory = new DefaultUriBuilderFactory();

        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                int apiDocPort = environment.getProperty("local.server.port", Integer.class, -1);
                String openApiPath = getEnvVariable("openApiEndpoint.path", String.class, "/v3/api-docs");
                UriBuilder uriBuilder = uriFactory.builder().scheme("http").host("localhost").path(openApiPath).port(apiDocPort);
                ResponseEntity<String> apiDocsResponse = getRestTemplate().getForEntity(uriBuilder.build(), String.class);
                if (apiDocsResponse.getStatusCode() == HttpStatus.OK) {
                    String apiDocFileName = getEnvVariable("file.name", "api-docs.json");
                    String apiDocFilePath = getEnvVariable("file.path", "build");
                    String responseBody = apiDocsResponse.getBody();
                    responseBody = replaceLocalEndpoint(responseBody, uriBuilder.replacePath("").build());
                    File apiDocFile = createApiDocFile(apiDocFileName, apiDocFilePath);
                    writeDocumentationInFile(apiDocFile, responseBody);
                }
            }

            private String replaceLocalEndpoint(String body, URI localUri) {
                String protocol = getEnvVariable("url.protocol", "http");
                String host = getEnvVariable("url.host", "localhost");
                int port = getEnvVariable("url.port", Integer.class, -1);
                UriBuilder externalBaseUrlBuilder = uriFactory.builder().scheme(protocol).host(host).port(port);
                body = body.replaceAll(localUri.toString(), externalBaseUrlBuilder.build().toString());
                return body;
            }

            private File createApiDocFile(String apiDocFileName, String apiDocFilePath) throws IOException {
                Path root = FileSystems.getDefault().getPath("").toAbsolutePath();
                Path filePath = Paths.get(root.toString(), apiDocFilePath, apiDocFileName);
                Files.createDirectories(filePath.getParent());
                //noinspection ResultOfMethodCallIgnored, result is irrelevant
                filePath.toFile().createNewFile();
                return filePath.toFile();
            }

            private String getEnvVariable(String envVariable, String defaultValue) {
                return environment.getProperty(APPLICATION_PROPERTIES_PREFIX + envVariable, defaultValue);
            }

            private <T> T getEnvVariable(String envVariable, Class<T> targetType, T defaultValue) {
                return environment.getProperty(APPLICATION_PROPERTIES_PREFIX + envVariable, targetType, defaultValue);
            }

            private void writeDocumentationInFile(File apiDocFile, String body) throws IOException {
                FileOutputStream outputStream = new FileOutputStream(apiDocFile);
                outputStream.write(body.getBytes());
            }

            private RestTemplate getRestTemplate() {
                return new RestTemplate();
            }
        };
    }
}


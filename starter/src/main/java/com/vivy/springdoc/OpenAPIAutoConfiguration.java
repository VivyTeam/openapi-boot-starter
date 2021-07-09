package com.vivy.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.SwaggerUiConfigParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotEmpty;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Configuration
@PropertySource("classpath:openapi.properties")
@AutoConfigureBefore(SwaggerUiConfigParameters.class)
@EnableConfigurationProperties(OpenAPIAutoConfiguration.AppConfiguration.class)
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
    public ApplicationRunner applicationRunner(AppConfiguration configProperties) {

        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                ResponseEntity<String> apiDocsResponse = getRestTemplate().getForEntity(String.format("http://localhost:%d/v3/api-docs", configProperties.getPort()), String.class);
                if (apiDocsResponse.getStatusCode() == HttpStatus.OK) {
                    String output = configProperties.output;
                    File apiDocFile = createApiDocFile(output);
                    writeDocumentationInFile(apiDocFile, apiDocsResponse.getBody());
                }
            }

            private File createApiDocFile(String file) throws IOException {
                Path root = FileSystems.getDefault().getPath("").toAbsolutePath();
                Path filePath = Paths.get(root.toString(), file);
                Files.createDirectories(filePath.getParent());
                //noinspection ResultOfMethodCallIgnored, output is not relevant
                filePath.toFile().createNewFile();
                return filePath.toFile();
            }

            private void writeDocumentationInFile(File apiDocFile, String body) throws IOException {
                try (FileOutputStream outputStream = new FileOutputStream(apiDocFile)) {
                    outputStream.write(body.getBytes());
                }
            }

            private RestTemplate getRestTemplate() {
                return new RestTemplate();
            }
        };
    }


    @Setter @Getter
    @ConfigurationProperties("openapi.service")
    public static class AppConfiguration {
        @Autowired
        private Environment env;

        private String output = "build/api-docs.json";

        public int getPort() {
            return env.getProperty("local.server.port", Integer.class, -1);
        }
    }
}

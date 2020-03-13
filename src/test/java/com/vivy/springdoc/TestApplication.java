package com.vivy.springdoc;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args).registerShutdownHook();
    }

    @Bean
    public OpenAPI openAPI() {
        return new BearerOpenAPI();
    }

}

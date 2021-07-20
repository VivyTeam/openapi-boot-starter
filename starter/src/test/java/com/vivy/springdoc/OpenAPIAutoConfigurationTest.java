package com.vivy.springdoc;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ReactiveWebApplicationContextRunner;
import static org.assertj.core.api.Assertions.assertThat;

class OpenAPIAutoConfigurationTest {

    private final ReactiveWebApplicationContextRunner contextRunner = new ReactiveWebApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(
                    OpenAPIAutoConfiguration.class
            ));

    @Test
    void shouldLoadOpenAPIConfiguration() {
        contextRunner.run(context -> assertThat(context).hasSingleBean(OpenAPIAutoConfiguration.class));
    }

    @Test
    void testThatApplicationRunnerIsPresentWhenOpenApiIsPresent() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(OpenAPI.class);
            assertThat(context).hasSingleBean(ApplicationRunner.class);
        });
    }
}

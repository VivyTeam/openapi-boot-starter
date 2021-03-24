package com.vivy.springdoc

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

class GenerateApiDocsTest extends DefaultTask {

    @Input
    String openApiDocsTestOut;

    @Input
    @Optional
    String testPackage = "com.vivy.openapi";

    @Input
    @Optional
    Class parentClass = null;

    @TaskAction
    def generate() {
            new File(openApiDocsTestOut).mkdirs()
            new File(openApiDocsTestOut, "OpenApiDocsTest.java").text =
                """package $testPackage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
${parentClass == null ? "" : "import $parentClass.name;"}

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OpenApiDocsTest${parentClass == null ? "" : " extends $parentClass.simpleName"} {

    @Autowired
    protected TestRestTemplate restTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void generateAPIDocs() throws Exception {
        var apiDocs = restTemplate.getForEntity("/v3/api-docs", Object.class);
        assertEquals(200, apiDocs.getStatusCodeValue());
        Files.writeString(Path.of("api-docs.json"), objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(apiDocs.getBody()));
    }
}
"""
    }
}

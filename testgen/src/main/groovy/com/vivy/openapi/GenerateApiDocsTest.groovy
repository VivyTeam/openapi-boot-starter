package com.vivy.openapi

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

class GenerateApiDocsTest extends DefaultTask {

    @Input
    String apiDocsTestOut;

    @Input
    @Optional
    String apiDocsFileOut = "build/api-docs.json";

    @Input
    @Optional
    String testPackage = "com.vivy.openapi";

    @Input
    String parentClass = null;

    @TaskAction
    def generate() {
        def names = parentClass.split("\\.")
        def parentClassSimpleName = names[names.length - 1];
        new File(apiDocsTestOut).mkdirs()
        new File(apiDocsTestOut, "OpenApiDocsTest.java").text =
            """package $testPackage;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import $parentClass;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OpenApiDocsTest extends $parentClassSimpleName {

    @Autowired
    protected TestRestTemplate restTemplate;

    @Test
    void generateAPIDocs() throws Exception {
        var apiDocs = restTemplate.getForEntity("/v3/api-docs", String.class);
        assertEquals(200, apiDocs.getStatusCodeValue());
        Files.writeString(Path.of("$apiDocsFileOut"), apiDocs.getBody());
    }
}
"""
    }
}

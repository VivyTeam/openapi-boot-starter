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
    @Optional
    Class parentClass = null;

    @TaskAction
    def generate() {
            new File(apiDocsTestOut).mkdirs()
            new File(apiDocsTestOut, "OpenApiDocsTest.java").text =
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

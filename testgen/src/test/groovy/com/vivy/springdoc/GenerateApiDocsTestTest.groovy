package com.vivy.openapi

import org.gradle.internal.impldep.org.junit.Assert;
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.Test;

class GenerateApiDocsTestTest {
    @Test
    void canAddTaskToProject() {
        def project = ProjectBuilder.builder().build()
        def task = project.task('generate', type: GenerateApiDocsTest)
        Assert.assertTrue(task instanceof GenerateApiDocsTest)
    }
}

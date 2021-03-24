package com.vivy.support;

import com.vivy.springdoc.scoped.startertest.StarterTestApplication;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        classes = StarterTestApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class AbstractIntegrationTest {
}

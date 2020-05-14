package com.vivy.springdoc.scoped;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication(scanBasePackages = "com.vivy.springdoc.scoped")
public class TestApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestApplication.class, args).registerShutdownHook();
    }

}

# Openapi-Boot-Starter

Creates a default JWT configuration of springdoc for springboot security. 

Include it in your project and initialize the bean using:

```java
@Bean
public OpenAPI openAPI() {
    return new BearerOpenAPI();
}
```
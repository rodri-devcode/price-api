package ar.com.rodrifernandez.priceapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Price API",
        version = "v1",
        description = "API to manage product prices and categories",
        contact = @Contact(name = "Support", email = "support@example.com"),
        license = @License(name = "MIT")
    ),
    servers = {@Server(url = "/")}
)
public class OpenApiConfig {
    // Empty - annotations configure OpenAPI metadata for Swagger UI
}
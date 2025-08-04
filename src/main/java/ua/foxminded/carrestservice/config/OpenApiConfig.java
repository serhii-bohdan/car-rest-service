package ua.foxminded.carrestservice.config;

import java.util.List;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ua.foxminded.carrestservice.util.openapi.OpenApiSpecProperties;

/**
 * Configuration class for OpenAPI documentation in the car rest service system.
 * Defines the OpenAPI specification, including server URL, API metadata (title, description, version, contact),
 * and security scheme for JWT-based authentication. Annotated with {@code @Configuration} to mark it as a
 * Spring configuration class and {@code @SecurityScheme} to define the Bearer JWT authentication scheme
 * for Swagger UI.
 *
 * @author Serhii Bohdan
 * @see org.springframework.context.annotation.Configuration
 * @see io.swagger.v3.oas.annotations.security.SecurityScheme
 * @see io.swagger.v3.oas.models.OpenAPI
 */
@Configuration
@SecurityScheme(name = "bearerAuth", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class OpenApiConfig {

    /**
     * Creates and configures the OpenAPI specification bean.
     * Defines the server URL and API metadata (title, description, version, contact email) using properties
     * from {@link OpenApiSpecProperties}. Used to generate Swagger UI documentation for the car rest service API.
     *
     * @param properties the {@link OpenApiSpecProperties} instance containing API configuration details
     * @return a configured {@link OpenAPI} instance
     */
    @Bean
    public OpenAPI customOpenApi(OpenApiSpecProperties properties) {
        return new OpenAPI()
            .servers(List.of(new Server()
                .url(properties.getServerUrl())
            ))
            .info(new Info()
                .title(properties.getTitle())
                .description(properties.getDescription())
                .version(properties.getVersion())
                .contact(new Contact()
                    .email(properties.getContactEmail())
                )
            );
    }

    /**
     * Creates and configures a bean for OpenAPI specification properties.
     * Provides an instance of {@link OpenApiSpecProperties} containing configuration details such as
     * server URL, API title, description, version, and contact email, used for OpenAPI documentation
     * in the car rest service system.
     *
     * @return a configured {@link OpenApiSpecProperties} instance
     */
    @Bean
    public OpenApiSpecProperties openApiSpecProperties() {
        return new OpenApiSpecProperties();
    }

}

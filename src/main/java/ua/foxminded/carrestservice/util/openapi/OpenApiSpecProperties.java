package ua.foxminded.carrestservice.util.openapi;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

/**
 * Configuration class for OpenAPI specification properties in the car rest service system.
 * This class holds the configuration properties required for generating the OpenAPI documentation,
 * such as server URL, API title, description, version, and contact email, loaded from
 * the application configuration file using Spring's {@code @Value} annotation.
 * Uses Lombok's {@code @Getter} annotation to generate getter methods for all fields.
 *
 * @author Serhii Bohdan
 * @see org.springframework.beans.factory.annotation.Value
 * @see lombok.Getter
 */
@Getter
public class OpenApiSpecProperties {

    /**
     * The URL of the server hosting the API.
     */
    @Value("${open-api.servers.url}")
    private String serverUrl;

    /**
     * The title of the API as displayed in the OpenAPI documentation.
     */
    @Value("${open-api.info.title}")
    private String title;

    /**
     * The description of the API as displayed in the OpenAPI documentation.
     */
    @Value("${open-api.info.description}")
    private String description;

    /**
     * The version of the API as displayed in the OpenAPI documentation.
     */
    @Value("${open-api.info.version}")
    private String version;

    /**
     * The contact email for the API as displayed in the OpenAPI documentation.
     */
    @Value("${open-api.info.contact.email}")
    private String contactEmail;

}

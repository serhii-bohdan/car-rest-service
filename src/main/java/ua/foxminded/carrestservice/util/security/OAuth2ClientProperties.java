package ua.foxminded.carrestservice.util.security;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

/**
 * Configuration class for OAuth2 client properties in the car rest service system.
 * This class holds the configuration properties required for OAuth2 authentication,
 * such as token access URL, client credentials, audience, and grant type, loaded from
 * the application configuration file using Spring's {@code @Value} annotation.
 * Uses Lombok's {@code @Getter} annotation to generate getter methods for all fields.
 *
 * @author Serhii Bohdan
 * @see org.springframework.beans.factory.annotation.Value
 * @see lombok.Getter
 */
@Getter
public class OAuth2ClientProperties {

    /**
     * The URL for accessing the OAuth2 token endpoint.
     */
    @Value("${security.oauth2.token.access.url}")
    private String tokenAccessUrl;

    /**
     * The client ID used for OAuth2 authentication.
     */
    @Value("${security.oauth2.client.id}")
    private String clientId;

    /**
     * The client secret used for OAuth2 authentication.
     */
    @Value("${security.oauth2.client.secret}")
    private String clientSecret;

    /**
     * The audience for the OAuth2 token request.
     */
    @Value("${security.oauth2.audience}")
    private String audience;

    /**
     * The grant type used for the OAuth2 token request, typically "client_credentials".
     */
    @Value("${security.oauth2.grant.type}")
    private String grantType;

}

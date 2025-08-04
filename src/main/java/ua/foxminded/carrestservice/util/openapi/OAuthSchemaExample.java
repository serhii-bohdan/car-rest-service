package ua.foxminded.carrestservice.util.openapi;

/**
 * Utility class providing JSON schema examples for OpenAPI documentation of OAuth-related endpoints.
 * Contains static constants with JSON strings representing responses for OAuth token operations
 * in the car rest service system. These examples are used to document the API in Swagger UI.
 *
 * @author Serhii Bohdan
 */
public class OAuthSchemaExample {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private OAuthSchemaExample() {
    }

    /**
     * JSON example of a response containing an OAuth access token.
     * Includes the access token, expiration time in seconds, and token type.
     */
    public static final String OAUTH_TOKEN_RESPONSE = """
        {
          "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
          "expiresIn": 86400,
          "tokenType": "Bearer"
        }
        """;

}
